package net.eabbott.verdi.util;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VerdiMath {
    public static Vec2 getAngle(Vec3 to, Vec3 from) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        Vec2 rot = new Vec3(dx, dy, dz).rotation();
        return new Vec2(rot.x, rot.y);
    }
}

/*
function dfCalc() {
    let e=parseInt(dfXstart.value),
        t=parseInt(dfZstart.value),
        n=parseInt(dfXend.value),
        a=parseInt(dfZend.value),

    d=e-n,
    c=t-a,
    l=180*Math.atan2(a-t,n-e)/Math.PI;

    dfDistance=Math.sqrt(d**2+c**2).toFixed(1)
    dfAng=l<-90?(l+270).toFixed(1):(l-90).toFixed(1)
* */