package doggytalents.common.talent;

import java.util.List;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.util.EntityUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

public class PuppyEyesTalent extends TalentInstance {

    private int cooldown;

    public PuppyEyesTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public TalentInstance copy() {
        PuppyEyesTalent inst = new PuppyEyesTalent(this.getTalent(), this.level);
        inst.cooldown = this.cooldown;
        return inst;
    }

    @Override
    public void init(AbstractDogEntity dogIn) {
        this.cooldown = dogIn.ticksExisted;
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.writeToNBT(dogIn, compound);
        int timeLeft = this.cooldown - dogIn.ticksExisted;
        compound.putInt("cooldown", timeLeft);
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.readFromNBT(dogIn, compound);
        this.cooldown = dogIn.ticksExisted + compound.getInt("cooldown");
    }

    // Left in for backwards compatibility for versions <= 2.0.0.5
    @Override
    public void onRead(AbstractDogEntity dogIn, CompoundNBT compound) {
        if (compound.contains("charmercharge")) {
            this.cooldown = dogIn.ticksExisted + compound.getInt("charmercharge");
        }
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (dogIn.ticksExisted % 40 != 0) {
            return;
        }

        if (dogIn.world.isRemote || !dogIn.isTamed()) {
            return;
        }

        if (this.level() <= 0) {
            return;
        }
        int timeLeft = this.cooldown - dogIn.ticksExisted;

        if (timeLeft <= 0) {
            LivingEntity owner = dogIn.getOwner();

            // Dog doesn't have owner or is offline
            if (owner == null) {
                return;
            }

            LivingEntity villager = this.getClosestVisibleVillager(dogIn, 5D);

            if (villager != null) {
                int rewardId = dogIn.getRNG().nextInt(this.level()) + (this.level() >= 5 ? 1 : 0);

                if (rewardId == 0) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.1.line.1", dogIn.getGenderPronoun()), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.1.line.2", dogIn.getGenderSubject()), villager.getUniqueID());
                    villager.entityDropItem(Items.PORKCHOP, 2);
                } else if (rewardId == 1) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.1", dogIn.getGenderTitle()), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.2", dogIn.getGenderTitle()), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.2.line.3", dogIn.getGenderTitle()), villager.getUniqueID());
                    villager.entityDropItem(Items.PORKCHOP, 5);
                } else if (rewardId == 2) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.1"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.2"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.3.line.3"), villager.getUniqueID());
                    villager.entityDropItem(Items.IRON_INGOT, 3);
                } else if (rewardId == 3) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.1"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.2"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.4.line.3"), villager.getUniqueID());
                    villager.entityDropItem(Items.GOLD_INGOT, 2);
                } else if (rewardId == 4) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.1"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.2"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.5.line.3"), villager.getUniqueID());
                    villager.entityDropItem(Items.DIAMOND, 1);
                } else if (rewardId == 5) {
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.1"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.2"), villager.getUniqueID());
                    owner.sendMessage(new TranslationTextComponent("talent.doggytalents.puppy_eyes.msg.6.line.3"), villager.getUniqueID());
                    villager.entityDropItem(Items.APPLE, 1);
                    villager.entityDropItem(Blocks.CAKE, 1);
                    villager.entityDropItem(Items.SLIME_BALL, 3);
                    villager.entityDropItem(Items.PORKCHOP, 5);
                }

                this.cooldown = dogIn.ticksExisted + (this.level() >= 5 ? 24000 : 48000);
            }
        }
    }

    public LivingEntity getClosestVisibleVillager(AbstractDogEntity dogIn, double radiusIn) {
        List<AbstractVillagerEntity> list = dogIn.world.getEntitiesWithinAABB(
            AbstractVillagerEntity.class,
            dogIn.getBoundingBox().grow(radiusIn, radiusIn, radiusIn),
            (village) -> village.canEntityBeSeen(dogIn)
        );

        return EntityUtil.getClosestTo(dogIn, list);
    }
}
