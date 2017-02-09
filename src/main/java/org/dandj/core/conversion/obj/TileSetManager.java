package org.dandj.core.conversion.obj;

import org.dandj.model.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniil on 09.02.17.
 */
public class TileSetManager {

    Map<Fragment, ObjGeometry> fragmentMap = new HashMap<>();

    public ObjGeometry createFragment(Fragment f, double x, double y) {
        ObjGeometry result = fragmentMap.get(f).duplicate();
        result.moveTo(x, y);
        return result;
    }

    public void addTileSet(ObjFile tileSet, double wallThickness) {
        double halfWidth = wallThickness / 2;
        double middle = 0.5;
        tileSet.getObjects().forEach(o -> {
            if (o.getName().startsWith("wall")) {
                ObjGeometry wallLeft = o.duplicate();
                wallLeft.moveTo(halfWidth, middle);
                fragmentMap.put(Fragment.WALL_L, wallLeft);

                ObjGeometry wallUp = o.duplicate();
                wallUp.rotate(Math.PI / 2);
                wallUp.moveTo(middle, halfWidth);
                fragmentMap.put(Fragment.WALL_U, wallUp);

                ObjGeometry wallRight = o.duplicate();
                wallRight.rotate(Math.PI);
                wallRight.moveTo(1 - halfWidth, middle);
                fragmentMap.put(Fragment.WALL_R, wallRight);

                ObjGeometry wallDown = o.duplicate();
                wallDown.rotate(-Math.PI / 2);
                wallDown.moveTo(middle, 1 - halfWidth);
                fragmentMap.put(Fragment.WALL_D, wallDown);
            } else if (o.getName().startsWith("corner-inner") || o.getName().startsWith("corner-outer")) {
                boolean inner = o.getName().startsWith("corner-inner");
                ObjGeometry upLeft = o.duplicate();
                upLeft.moveTo(halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UL_INNER : Fragment.CORNER_UL_OUTER, upLeft);

                ObjGeometry upRight = o.duplicate();
                upRight.rotate(Math.PI / 2);
                upRight.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_UR_INNER : Fragment.CORNER_UR_OUTER, upRight);

                ObjGeometry downRight = o.duplicate();
                downRight.rotate(Math.PI);
                downRight.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? Fragment.CORNER_DR_INNER : Fragment.CORNER_DR_OUTER, downRight);

                ObjGeometry downLeft = o.duplicate();
                downLeft.rotate(-Math.PI / 2);
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
                upLeftHorizontal.rotate(Math.PI / 2);
                upLeftHorizontal.moveTo(halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UL_H, upLeftHorizontal);

                ObjGeometry upRightVertical = o.duplicate();
                upRightVertical.rotate(Math.PI);
                upRightVertical.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_V, upRightVertical);

                ObjGeometry upRightHorizontal = o.duplicate();
                upRightHorizontal.rotate(Math.PI / 2);
                upRightHorizontal.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(Fragment.CORNER_UR_H, upRightHorizontal);

                ObjGeometry downRightVertical = o.duplicate();
                downRightVertical.rotate(Math.PI);
                downRightVertical.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_V, downRightVertical);

                ObjGeometry downRightHorizontal = o.duplicate();
                downRightHorizontal.rotate(-Math.PI / 2);
                downRightHorizontal.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DR_H, downRightHorizontal);

                ObjGeometry downLeftVertical = o.duplicate();
                downLeftVertical.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_V, downLeftVertical);

                ObjGeometry downLeftHorizontal = o.duplicate();
                downLeftHorizontal.rotate(-Math.PI / 2);
                downLeftHorizontal.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(Fragment.CORNER_DL_H, downLeftHorizontal);
            }
        });
    }
}
