package com.songoda.ultimatetimber.hooks;

import java.lang.reflect.Method;
import java.util.Set;

import com.songoda.ultimatetimber.tree.TreeBlock;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.config.experience.ExperienceConfig;

public class McMMOHook implements TimberHook {

    private Enum<?> woodcuttingEnum;
    private Method getXpMethod;
    
    public McMMOHook() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
        try { // Try to find mcMMO Overhaul
            Class<?> primarySkillTypeClass = Class.forName("com.gmail.nossr50.datatypes.skills.PrimarySkillType");
            for (Object enumValue : primarySkillTypeClass.getEnumConstants()) {
                Enum<?> primarySkillTypeEnum = (Enum<?>) enumValue;
                if (primarySkillTypeEnum.name().equals("WOODCUTTING")) {
                    woodcuttingEnum = primarySkillTypeEnum;
                    break;
                }
            }
            getXpMethod = ExperienceConfig.class.getMethod("getXp", woodcuttingEnum.getClass(), Material.class);
        } catch (Exception ex) {
            Class<?> skillTypeClass = Class.forName("com.gmail.nossr50.datatypes.skills.SkillType");
            for (Object enumValue : skillTypeClass.getEnumConstants()) {
                Enum<?> skillTypeEnum = (Enum<?>) enumValue;
                if (skillTypeEnum.name().equals("WOODCUTTING")) {
                    woodcuttingEnum = skillTypeEnum;
                    break;
                }
            }
            getXpMethod = ExperienceConfig.class.getMethod("getXp", woodcuttingEnum.getClass(), Material.class);
        }
    }

    @Override
    public void apply(Player player, Set<TreeBlock> treeBlocks) throws Exception {
        if (player.getGameMode().equals(GameMode.CREATIVE)) 
            return;
        
        int xp = 0;
        for (Block block : treeBlocks) {
            Material material = WoodToLogConverter.convert(block.getType());
            if (!material.name().endsWith("LOG")) continue;
            xp += (int) getXpMethod.invoke(ExperienceConfig.getInstance(), woodcuttingEnum, material);
        }
        
        ExperienceAPI.addXP(player, "woodcutting", xp, "pve");
    }

}
