package dev.kofeychi.Cart.API;

import dev.kofeychi.Cart.Cart;
import dev.kofeychi.Cart.SSModule.EnabledAffections;
import dev.kofeychi.Cart.SSModule.PSI;
import dev.kofeychi.Cart.SSModule.SSModes;
import dev.kofeychi.Cart.SSModule.SSPacket;
import dev.kofeychi.Cart.Util.Easing;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.joml.Vector3f;

public class Example {
    public static void ExampleInstance(){
        /*
        Make sure you understand how EnabledAffections class work!
        new EnabledAffections("nnnyyy") -- is the example of object

        the first three symbols resemble rotation axises (XYZ)
        the second three symbols resemble position axises (XYZ)

        the options to enable or disable the axis is "n" for disable and "y" for enable

         */
        Vector3f vec = new Vector3f((float) 2 / 50, (float) 2 / 50, (float) 2 / 50).mul(4); // theres definetely a big problem with intensity values so i just make them low
        Cart.SERVER.getPlayerManager().getPlayerList().forEach((spe)-> ServerPlayNetworking.send(spe,new SSPacket(Cart.GSON.toJson(new PSI(120/* duration*/, SSModes.SSEase.LINEAR/* idk never tested but should work*/, SSModes.SSRng.PERLIN/* idk if it works*/, new EnabledAffections("nnyyyy"),spe.getPos()/* PSI pos*/,5,15)
                        .setRot1(vec)//starting rotation value
                        .setRot2(new Vector3f(0, 0, 0))//end rotation value
                        .setPos1(vec)//starting position value
                        .setPos2(new Vector3f(0, 0, 0))//end position value
                        .setLinearCurve(Easing.QUAD_OUT) //i hate this stuff honestly (Easings.net cheat sheet)
                        .setPerlinSpeedI(.5f) //needed with perlin only
                ),"psi"//describes type of instance
                )
        ));
    }
}
