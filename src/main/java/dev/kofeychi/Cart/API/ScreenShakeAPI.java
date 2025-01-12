package dev.kofeychi.Cart.API;

import dev.kofeychi.Cart.Cart;
import dev.kofeychi.Cart.SSModule.SSInstance;
import dev.kofeychi.Cart.SSModule.SSPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class ScreenShakeAPI {
    public static void SendScreenShakeToPlayers(List<ServerPlayerEntity> Players, SSInstance Instance) {
        if (Objects.equals(Instance.getType(), "PSI")){
            Players.forEach((spe)-> ServerPlayNetworking.send(spe,new SSPacket(Cart.GSON.toJson(Instance),"psi")));
        }
        if (Objects.equals(Instance.getType(), "SSInstance")){
            Players.forEach((spe)-> ServerPlayNetworking.send(spe,new SSPacket(Cart.GSON.toJson(Instance),"ssi")));
        }
    }
}
