package org.dandj.core.conversion.obj;

import com.jme3.math.FastMath;
import org.dandj.model.Fragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.dandj.model.Fragment.*;

public class TileSetManager {
    private long objectCounter = 0;
    private Map<String, Map<Fragment, ObjGeometry>> tileSetMap = new HashMap<>();
    private ObjGeometry door;
    private ObjGeometry gates;
    private ObjGeometry trigger;

    public ObjGeometry createFragment(String name, String style, Fragment f, float x, float y) {
        ObjGeometry result = tileSetMap.get(style).get(f).duplicate(name + f.name() + objectCounter++);
        if (result != null)
            result.moveTo(x, y);
        return result;
    }

    public void addTileSet(ObjFile tileSet, String style, float wallThickness) {
        Set<String> existingMaterialNames = new HashSet<>();
        tileSetMap.values().forEach(m ->
                existingMaterialNames.addAll(m.values().stream().map(o ->
                        o.getMaterial().getName()).collect(toList())));
        float halfWidth = wallThickness / 2;
        float middle = 0.5f;
        Map<Fragment, ObjGeometry> fragmentMap = new HashMap<>();
        tileSet.getObjects().forEach(o -> {
            if (existingMaterialNames.contains(o.getMaterial().getName())) {
                while (existingMaterialNames.contains(o.getMaterial().getName() + objectCounter)) {
                    objectCounter++;
                }
                o.getMaterial().setName(o.getMaterial().getName() + objectCounter);
            }
            if (o.getName().startsWith("wall")) {
                ObjGeometry wallLeft = o.duplicate(WALL_L.name());
                wallLeft.moveTo(halfWidth, middle);
                fragmentMap.put(WALL_L, wallLeft);

                ObjGeometry wallUp = o.duplicate(WALL_U.name());
                wallUp.rotateY(-FastMath.HALF_PI);
                wallUp.moveTo(middle, halfWidth);
                fragmentMap.put(WALL_U, wallUp);

                ObjGeometry wallRight = o.duplicate(WALL_R.name());
                wallRight.rotateY(-FastMath.PI);
                wallRight.moveTo(1 - halfWidth, middle);
                fragmentMap.put(WALL_R, wallRight);

                ObjGeometry wallDown = o.duplicate(WALL_D.name());
                wallDown.rotateY(FastMath.HALF_PI);
                wallDown.moveTo(middle, 1 - halfWidth);
                fragmentMap.put(WALL_D, wallDown);
            } else if (o.getName().startsWith("corner-inner") || o.getName().startsWith("corner-outer")) {
                boolean inner = o.getName().startsWith("corner-inner");
                ObjGeometry upLeft = o.duplicate((inner ? CORNER_UL_INNER : CORNER_UL_OUTER).name());
                upLeft.moveTo(halfWidth, halfWidth);
                fragmentMap.put(inner ? CORNER_UL_INNER : CORNER_UL_OUTER, upLeft);

                ObjGeometry upRight = o.duplicate((inner ? CORNER_UR_INNER : CORNER_UR_OUTER).name());
                upRight.rotateY(-FastMath.HALF_PI);
                upRight.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(inner ? CORNER_UR_INNER : CORNER_UR_OUTER, upRight);

                ObjGeometry downRight = o.duplicate((inner ? CORNER_DR_INNER : CORNER_DR_OUTER).name());
                downRight.rotateY(-FastMath.PI);
                downRight.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? CORNER_DR_INNER : CORNER_DR_OUTER, downRight);

                ObjGeometry downLeft = o.duplicate((inner ? CORNER_DL_INNER : CORNER_DL_OUTER).name());
                downLeft.rotateY(FastMath.HALF_PI);
                downLeft.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(inner ? CORNER_DL_INNER : CORNER_DL_OUTER, downLeft);
            } else if (o.getName().startsWith("floor")) {
                ObjGeometry floor = o.duplicate(FLOOR.name());
                floor.moveTo(middle, middle);
                fragmentMap.put(FLOOR, floor);
            } else if (o.getName().startsWith("ceiling")) {
                ObjGeometry ceiling = o.duplicate(CEILING.name());
                ceiling.moveTo(middle, middle);
                fragmentMap.put(CEILING, ceiling);
            } else if (o.getName().startsWith("filler")) {
                ObjGeometry upLeftVertical = o.duplicate(CORNER_UL_V.name());
                upLeftVertical.moveTo(halfWidth, halfWidth);
                fragmentMap.put(CORNER_UL_V, upLeftVertical);

                ObjGeometry upLeftHorizontal = o.duplicate(CORNER_UL_H.name());
                upLeftHorizontal.rotateY(-FastMath.HALF_PI);
                upLeftHorizontal.moveTo(halfWidth, halfWidth);
                fragmentMap.put(CORNER_UL_H, upLeftHorizontal);

                ObjGeometry upRightVertical = o.duplicate(CORNER_UR_V.name());
                upRightVertical.rotateY(-FastMath.PI);
                upRightVertical.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(CORNER_UR_V, upRightVertical);

                ObjGeometry upRightHorizontal = o.duplicate(CORNER_UR_H.name());
                upRightHorizontal.rotateY(-FastMath.HALF_PI);
                upRightHorizontal.moveTo(1 - halfWidth, halfWidth);
                fragmentMap.put(CORNER_UR_H, upRightHorizontal);

                ObjGeometry downRightVertical = o.duplicate(CORNER_DR_V.name());
                downRightVertical.rotateY(-FastMath.PI);
                downRightVertical.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(CORNER_DR_V, downRightVertical);

                ObjGeometry downRightHorizontal = o.duplicate(CORNER_DR_H.name());
                downRightHorizontal.rotateY(FastMath.HALF_PI);
                downRightHorizontal.moveTo(1 - halfWidth, 1 - halfWidth);
                fragmentMap.put(CORNER_DR_H, downRightHorizontal);

                ObjGeometry downLeftVertical = o.duplicate(CORNER_DL_V.name());
                downLeftVertical.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(CORNER_DL_V, downLeftVertical);

                ObjGeometry downLeftHorizontal = o.duplicate(CORNER_DL_H.name());
                downLeftHorizontal.rotateY(FastMath.HALF_PI);
                downLeftHorizontal.moveTo(halfWidth, 1 - halfWidth);
                fragmentMap.put(CORNER_DL_H, downLeftHorizontal);
            }
        });
        tileSetMap.put(style, fragmentMap);
    }

    public void addJunction(ObjFile objFile) {
        objFile.getObjects().forEach(o -> {
            if (o.getName().startsWith("door"))
                door = o;
            else if (o.getName().startsWith("gates"))
                gates = o;
            else if (o.getName().startsWith("trigger"))
                trigger = o;
        });
    }

    public ObjGeometry createJunction(float x, float y, float angle) {
        ObjGeometry newJunction = gates.duplicate("junction" + objectCounter++);
        newJunction.rotateY(angle);
        newJunction.moveTo(x, y);
        return newJunction;
    }
}
