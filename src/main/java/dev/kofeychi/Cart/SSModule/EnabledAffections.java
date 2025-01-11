package dev.kofeychi.Cart.SSModule;

public class EnabledAffections {
    public boolean RotX,PosX;
    public boolean RotY,PosY;
    public boolean RotZ,PosZ;

    public EnabledAffections(String encoded){
        RotX = Char(encoded.charAt(0));
        RotY = Char(encoded.charAt(1));
        RotZ = Char(encoded.charAt(2));
        PosX = Char(encoded.charAt(3));
        PosY = Char(encoded.charAt(4));
        PosZ = Char(encoded.charAt(5));
    }
    public static boolean Char(Character character){
        return character.equals('y');
    }
    public static char Char(boolean b){
        return b ? 'y' : 'n';
    }

    @Override
    public String toString() {
        return ""+Char(RotX)+Char(RotY)+Char(RotZ)+Char(PosX)+Char(PosY)+Char(PosZ);
    }
}
