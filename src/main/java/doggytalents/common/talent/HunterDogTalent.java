package doggytalents.common.talent;

import doggytalents.DoggyTalents;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class HunterDogTalent {

    public static void onLootDrop(final LootingLevelEvent event) {
        Entity trueSource = event.getDamageSource().getTrueSource();
        if (trueSource instanceof DogEntity) {
            DogEntity dog = (DogEntity) trueSource;
            int level = dog.getLevel(DoggyTalents.HUNTER_DOG);

            if (dog.getRNG().nextInt(6) < level + (level >= 5 ? 1 : 0)) {
                event.setLootingLevel(event.getLootingLevel() + level / 2);
            }

        }
    }
}
