package io.github.vladocc.getbacktohome;

import net.minecraftforge.common.config.Config;

/**
 * Created by Voyager on 07.05.2018.
 */

@Config(modid = ModInfo.MODID)
public class ModConfig {

    @Config.Comment({"Ways of home creation:",
            "   0 - you sets your home only when you sleeping and you can not go home if your bed is destroyed",
            "   1 - you can use command /sethome ot /createhome to set position of your home",
            "   2 - you sets your home by sleeping or typing chat command (/sethome, /createhome)",
            "Default: 1"})
    @Config.Name("home_creation")
    @Config.RangeInt(min = 0, max = 2)
    public static int homeType = 1;

    @Config.Comment({"How you will pay for the way:",
            "   true - Your exhaustion will be automatically removed from your saturation and hunger bars.",
            "       If you do not have enough of hunger, mod would it as much food from your toolbar as needed for teleportation.",
            "       If you do not have enough food on toolbar, then you will not be teleported and no of your parameters will be changed",
            "   false - Your teleportation to home is guaranteed, but you will be very exhausted.",
            "       Journey cost will be added to your vanilla exhaustion parameter",
            "Default: false"})
    @Config.Name("automatically_eat_food_in_journey")
    public static boolean paymentType = false;

    @Config.Comment({"Cost of horizontal move for one block",
            "Default: 0.1"})
    @Config.Name("cost_for_one_meter_horizontally")
    @Config.RangeDouble(min = 0, max = 10)
    public static double runCost = 0.1;

    @Config.Comment({"Cost of jump up for one block",
            "Default: 0.2"})
    @Config.Name("cost_for_one_meter_up")
    @Config.RangeDouble(min = 0, max = 10)
    public static double jumpCost = 0.2;

    @Config.Comment({"Cost of fall for one block",
            "Default: 0"})
    @Config.Name("cost_for_one_meter_down")
    @Config.RangeDouble(min = 0, max = 10)
    public static double fallCost = 0;

    @Config.Comment({"Cost of all distance from your position to home will be multiplied by this number",
            "Default: 1.0"})
    @Config.Name("cost_multiplier")
    @Config.RangeDouble(min = 1.0, max = 100)
    public static double multCost = 1.0;

    @Config.Comment({"If this is true, you can teleport when falling",
            "Default: false"})
    @Config.Name("teleport_in_fall")
    public static boolean teleportInFall = false;

    @Config.Comment("If this is false, you can teleport only if no mobs in 5 meter radius near you")
    @Config.Name("teleport_near_monster")
    public static boolean nearMonster = false;

    @Config.Comment({"Delaying control:",
            "   0 - No delay before teleportation",
            "   1 - 3 seconds delay before teleportation",
            "   2 - 3 seconds delay before teleportation. You will not teleport if in this time you will be damaged or moved",
            "Default: 0"})
    @Config.Name("delaying")
    @Config.RangeInt(min = 0, max = 2)
    public static int delaying = 0;

    @Config.Comment({"If this is true, when you are mounted and teleporting to home, you will be teleported with your mount.",
            "If this is false you will be dismounted",
            "Default: true"})
    @Config.Name("teleport_with_mount")
    public static boolean onMount = false;

    @Config.Comment({"Cost of your teleportation will be multiplied by this number, if you are mounted and teleporting with your mount",
            "Default: 2.0"})
    @Config.Name("cost_on_mount_multiplier")
    @Config.RangeDouble(min = 1.0, max = 100)
    public static double multMountCost = 2.0;
}
