package org.dandj.core.conversion.obj;

import com.jme3.math.FastMath;
import org.dandj.model.Fragment;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TileSetManager {

    private Map<String, Map<Fragment, ObjGeometry>> tileSetMap = new HashMap<>();

    public ObjGeometry createFragment(@Nonnull String style, Fragment f, float x, float y) {
        ObjGeometry result = tileSetMap.get(style).get(f).duplicate();
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
                ObjGeometry wallLeft = o.duplicate();
                wallLeft.moveTo(halfWidth, middle);
                fragmentMap.put(Fragment.WALL_L, wallLeft);

                ObjGeometry wallUp = o.duplicate();
                wallUp.rotate(FastMath.HALF_PI);
                wallUp.moveTo(middle, halfWidth);
                fragmentMap.put(Fragment.WALL_U, wallUp);

                ObjGeometry wallRight = o.duplicate();
                wallRight.rotate(FastMath.PI);
                wallRight.moveTo(1 - halfWidth, middle);
                fragmentMap.put(Fragment.WALL_R, wallRight);

                ObjGeometry wallDown = o.duplicate();
                wallDown.rotate(-FastMath.HALF_PI);
                wallDown.moveTo(middle, 1 - halfWidth);
                fragmentMap.put(Fragment.WALL_D, wallDown);
            } else if (o.getName().startsWith("corner-inner") || o.getName().startsWith("corner-outer")) {
                boolean inner = o.getName().startsWith("corner-inner");
                ObjGeometry upLeft = o.duplicate();
                upLeft.moveTo(halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UL_INNER : Fragment.CORNER_UL_OUTER, upLeft);

                ObjGeometry upRight = o.duplicate();
                upRight.rotate(FastMath.HALF_PI);
                upRight.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UR_INNER : Fragment.CORNER_UR_OUTER, upRight);

                ObjGeometry downRight = o.duplicate();
                downRight.rotate(FastMath.PI);
                downRight.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_DR_INNER : Fragment.CORNER_DR_OUTER, downRight);

                ObjGeometry downLeft = o.duplicate();
                downLeft.rotate(-FastMath.HALF_PI);
                downLeft.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_DL_INNER : Fragment.CORNER_DL_OUTER, downLeft);
            } else if (o.getName().startsWith("floor")) {
                ObjGeometry floor = o.duplicate();
                floor.moveTo(middle, middle);
                fragmentMap.put(Fragment.FLOOR, floor);
            } else if (o.getName().startsWith("filler")) {
                ObjGeometry upLeftVertical = o.duplicate();
                upLeftVertical.moveTo(halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UL_V, upLeftVertical);

                ObjGeometry upLeftHorizontal = o.duplicate();
                upLeftHorizontal.rotate(FastMath.HALF_PI);
                upLeftHorizontal.moveTo(halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UL_H, upLeftHorizontal);

                ObjGeometry upRightVertical = o.duplicate();
                upRightVertical.rotate(FastMath.PI);
                upRightVertical.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_V, upRightVertical);

                ObjGeometry upRightHorizontal = o.duplicate();
                upRightHorizontal.rotate(FastMath.HALF_PI);
                upRightHorizontal.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_H, upRightHorizontal);

                ObjGeometry downRightVertical = o.duplicate();
                downRightVertical.rotate(FastMath.PI);
                downRightVertical.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_V, downRightVertical);

                ObjGeometry downRightHorizontal = o.duplicate();
                downRightHorizontal.rotate(-FastMath.HALF_PI);
                downRightHorizontal.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_H, downRightHorizontal);

                ObjGeometry downLeftVertical = o.duplicate();
                downLeftVertical.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_V, downLeftVertical);

                ObjGeometry downLeftHorizontal = o.duplicate();
                downLeftHorizontal.rotate(-FastMath.HALF_PI);
                downLeftHorizontal.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_H, downLeftHorizontal);
            }
        });
        tileSetMap.put(style, fragmentMap);
    }
}
