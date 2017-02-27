package org.dandj.core.conversion.obj;

import com.jme3.math.FastMath;
import org.dandj.model.Fragment;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TileSetManager {

    private Map<String, Map<Fragment, ObjGeometry>> tileSetMap = new HashMap<>();

    public ObjGeometry createFragment(@Nonnull String style, Fragment f, float x, float y) {
        ObjGeometry result = tileSetMap.get(style).get(f).duplicate(f);
        if (result != null)
            result.moveTo(x, y);
        return result;
    }

    public void addTileSet(ObjFile tileSet, String style, float wallThickness) {
        float halfWidth = wallThickness / 2;
        float middle = 0.5f;
        Map<Fragment, ObjGeometry> fragmentMap = new HashMap<>();
        tileSet.getObjects().forEach(o -> {
            if (o.getName().startsWith("wall")) {
                ObjGeometry wallLeft = o.duplicate(Fragment.WALL_L);
                wallLeft.moveTo(halfWidth, middle);
                fragmentMap.put(Fragment.WALL_L, wallLeft);

                ObjGeometry wallUp = o.duplicate(Fragment.WALL_U);
                wallUp.rotateY(-FastMath.HALF_PI);
                wallUp.moveTo(middle, halfWidth);
                fragmentMap.put(Fragment.WALL_U, wallUp);

                ObjGeometry wallRight = o.duplicate(Fragment.WALL_R);
                wallRight.rotateY(-FastMath.PI);
                wallRight.moveTo(1 - halfWidth, middle);
                fragmentMap.put(Fragment.WALL_R, wallRight);

                ObjGeometry wallDown = o.duplicate(Fragment.WALL_D);
                wallDown.rotateY(FastMath.HALF_PI);
                wallDown.moveTo(middle, 1 - halfWidth);
                fragmentMap.put(Fragment.WALL_D, wallDown);
            } else if (o.getName().startsWith("corner-inner") || o.getName().startsWith("corner-outer")) {
                boolean inner = o.getName().startsWith("corner-inner");
                ObjGeometry upLeft = o.duplicate(inner ? Fragment.CORNER_UL_INNER : Fragment.CORNER_UL_OUTER);
                upLeft.moveTo(halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UL_INNER : Fragment.CORNER_UL_OUTER, upLeft);

                ObjGeometry upRight = o.duplicate(inner ? Fragment.CORNER_UR_INNER : Fragment.CORNER_UR_OUTER);
                upRight.rotateY(-FastMath.HALF_PI);
                upRight.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UR_INNER : Fragment.CORNER_UR_OUTER, upRight);

                ObjGeometry downRight = o.duplicate(inner ? Fragment.CORNER_DR_INNER : Fragment.CORNER_DR_OUTER);
                downRight.rotateY(-FastMath.PI);
                downRight.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_DR_INNER : Fragment.CORNER_DR_OUTER, downRight);

                ObjGeometry downLeft = o.duplicate(inner ? Fragment.CORNER_DL_INNER : Fragment.CORNER_DL_OUTER);
                downLeft.rotateY(FastMath.HALF_PI);
                downLeft.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_DL_INNER : Fragment.CORNER_DL_OUTER, downLeft);
            } else if (o.getName().startsWith("floor")) {
                ObjGeometry floor = o.duplicate(Fragment.FLOOR);
                floor.moveTo(middle, middle);
                fragmentMap.put(Fragment.FLOOR, floor);
            } else if (o.getName().startsWith("filler")) {
                ObjGeometry upLeftVertical = o.duplicate(Fragment.CORNER_UL_V);
                upLeftVertical.moveTo(halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UL_V, upLeftVertical);

                ObjGeometry upLeftHorizontal = o.duplicate(Fragment.CORNER_UL_H);
                upLeftHorizontal.rotateY(-FastMath.HALF_PI);
                upLeftHorizontal.moveTo(halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UL_H, upLeftHorizontal);

                ObjGeometry upRightVertical = o.duplicate(Fragment.CORNER_UR_V);
                upRightVertical.rotateY(-FastMath.PI);
                upRightVertical.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_V, upRightVertical);

                ObjGeometry upRightHorizontal = o.duplicate(Fragment.CORNER_UR_H);
                upRightHorizontal.rotateY(-FastMath.HALF_PI);
                upRightHorizontal.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_H, upRightHorizontal);

                ObjGeometry downRightVertical = o.duplicate(Fragment.CORNER_DR_V);
                downRightVertical.rotateY(-FastMath.PI);
                downRightVertical.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_V, downRightVertical);

                ObjGeometry downRightHorizontal = o.duplicate(Fragment.CORNER_DR_H);
                downRightHorizontal.rotateY(FastMath.HALF_PI);
                downRightHorizontal.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_H, downRightHorizontal);

                ObjGeometry downLeftVertical = o.duplicate(Fragment.CORNER_DL_V);
                downLeftVertical.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_V, downLeftVertical);

                ObjGeometry downLeftHorizontal = o.duplicate(Fragment.CORNER_DL_H);
                downLeftHorizontal.rotateY(FastMath.HALF_PI);
                downLeftHorizontal.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_H, downLeftHorizontal);
            }
        });
        tileSetMap.put(style, fragmentMap);
    }
}
