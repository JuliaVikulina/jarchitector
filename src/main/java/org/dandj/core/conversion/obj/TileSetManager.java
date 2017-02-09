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

    public void addTileSet(ObjFile tileSet) {
        double big = 0.5;
        double small = 0.25;
        tileSet.getObjects().forEach(o -> {
            if (o.getName().startsWith("wall")) {
                ObjGeometry wallLeft = o.duplicate();
                wallLeft.moveTo(small, big);
                fragmentMap.put(Fragment.WALL_L, wallLeft);

                ObjGeometry wallUp = o.duplicate();
                wallUp.moveTo(big, small);
                fragmentMap.put(Fragment.WALL_U, wallUp);

                ObjGeometry wallRight = o.duplicate();
                wallRight.moveTo(1 - small, big);
                fragmentMap.put(Fragment.WALL_R, wallRight);

                ObjGeometry wallDown = o.duplicate();
                wallDown.moveTo(big, 1 - small);
                fragmentMap.put(Fragment.WALL_D, wallDown);
            } else if (o.getName().startsWith("corner-inner") || o.getName().startsWith("corner-outer")) {
                boolean inner = o.getName().startsWith("corner-inner");
                ObjGeometry upLeft = o.duplicate();
                upLeft.moveTo(small, small);
                fragmentMap.put(inner ? Fragment.CORNER_UL_INNER : Fragment.CORNER_UL_OUTER, upLeft);

                ObjGeometry upRight = o.duplicate();
                upRight.moveTo(1 - small, small);
                fragmentMap.put(inner ? Fragment.CORNER_UR_INNER : Fragment.CORNER_UR_OUTER, upRight);

                ObjGeometry downRight = o.duplicate();
                downRight.moveTo(1 - small, 1 - small);
                fragmentMap.put(inner ? Fragment.CORNER_DR_INNER : Fragment.CORNER_DR_OUTER, downRight);

                ObjGeometry downLeft = o.duplicate();
                downLeft.moveTo(small, 1 - small);
                fragmentMap.put(inner ? Fragment.CORNER_DL_INNER : Fragment.CORNER_DL_OUTER, downLeft);
            } else if (o.getName().startsWith("floor")) {
                ObjGeometry floor = o.duplicate();
                floor.moveTo(big, big);
                fragmentMap.put(Fragment.FLOOR, floor);
            } else if (o.getName().startsWith("filler")) {
                ObjGeometry upLeftVertical = o.duplicate();
                upLeftVertical.moveTo(small, small);
                fragmentMap.put(Fragment.CORNER_UL_V, upLeftVertical);

                ObjGeometry upLeftHorizontal = o.duplicate();
                upLeftHorizontal.moveTo(small, small);
                fragmentMap.put(Fragment.CORNER_UL_H, upLeftHorizontal);

                ObjGeometry upRightVertical = o.duplicate();
                upRightVertical.moveTo(1 - small, small);
                fragmentMap.put(Fragment.CORNER_UR_V, upRightVertical);

                ObjGeometry upRightHorizontal = o.duplicate();
                upRightHorizontal.moveTo(1 - small, small);
                fragmentMap.put(Fragment.CORNER_UR_H, upRightHorizontal);

                ObjGeometry downRightVertical = o.duplicate();
                downRightVertical.moveTo(1 - small, 1 - small);
                fragmentMap.put(Fragment.CORNER_DR_V, downRightVertical);

                ObjGeometry downRightHorizontal = o.duplicate();
                downRightHorizontal.moveTo(1 - small, 1 - small);
                fragmentMap.put(Fragment.CORNER_DR_H, downRightHorizontal);

                ObjGeometry downLeftVertical = o.duplicate();
                downLeftVertical.moveTo(small, 1 - small);
                fragmentMap.put(Fragment.CORNER_DL_V, downLeftVertical);

                ObjGeometry downLeftHorizontal = o.duplicate();
                downLeftHorizontal.moveTo(small, 1 - small);
                fragmentMap.put(Fragment.CORNER_DL_H, downLeftHorizontal);
            }
        });
    }
}
