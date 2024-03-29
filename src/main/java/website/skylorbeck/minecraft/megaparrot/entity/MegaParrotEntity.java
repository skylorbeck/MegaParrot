package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.mixin.HorseBaseEntityAccessor;
import website.skylorbeck.minecraft.skylorlib.IMegable;

import java.util.Arrays;
import java.util.UUID;

public class MegaParrotEntity extends AbstractHorseEntity implements IAnimatable {
    private static final Item[] BREEDING_INGREDIENT = {Items.WHEAT_SEEDS,Items.MELON_SEEDS,Items.BEETROOT_SEEDS,Items.PUMPKIN_SEEDS, Items.APPLE, Items.CARROT,Items.BEETROOT,Items.POTATO, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE};
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(MegaParrotEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> FALLING = DataTracker.registerData(MegaParrotEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected int soundTicks;
    protected int eatingTicks = 0;
    private static final UUID HORSE_ARMOR_BONUS_ID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    private float flapSpeed = 1.0f;

    private int featherDropTime = this.random.nextInt(6000)+6000;
    public MegaParrotEntity(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initAttributes(Random random) {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(this.getChildHealthBonus());
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(this.getChildMovementSpeedBonus());
        this.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(this.getChildJumpStrengthBonus());
//        Logger.getGlobal().log(Level.SEVERE,this.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).getValue()+":jump "+this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()+":speed "+this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getValue()+":health ");
    }
    protected float getChildHealthBonus() {
        return 15.0f + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
    }

    protected double getChildJumpStrengthBonus() {//min 0.3 max 0.7 //horse is 0.4/1.0
        return (double)0.3f + this.random.nextDouble() * 0.2 + this.random.nextDouble() * 0.1 + this.random.nextDouble() * 0.1;
    }

    protected double getChildMovementSpeedBonus() {//min 0.2 max 0.4 //horse is .1125/.3375
        return ((double)0.8f + this.random.nextDouble() * 0.2 + this.random.nextDouble() * 0.2 + this.random.nextDouble() * 0.2) * 0.25;
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(FALLING, 0f);
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getVariant());
        if (!this.items.getStack(1).isEmpty()) {
            nbt.put("ArmorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
        }
        nbt.putInt("FeatherDropTime", this.featherDropTime);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        ItemStack itemStack;
        super.readCustomDataFromNbt(nbt);
        this.setVariant(nbt.getInt("Variant"));
        if (nbt.contains("ArmorItem", 10) && !(itemStack = ItemStack.fromNbt(nbt.getCompound("ArmorItem"))).isEmpty() && this.isHorseArmor(itemStack)) {
            this.items.setStack(1, itemStack);
        }
        if (nbt.contains("FeatherDropTime")) {
            this.featherDropTime = nbt.getInt("FeatherDropTime");
        }
        this.updateSaddle();
    }

    public void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }
    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.isBaby()) {
            if (this.isTame() && player.shouldCancelInteraction()) {
                this.openInventory(player);
                return ActionResult.success(this.world.isClient);
            }
            if (this.hasPassengers()) {
                return super.interactMob(player, hand);
            }
        }
        if (!itemStack.isEmpty()) {
            boolean bl;
            if (this.isBreedingItem(itemStack)) {
                return this.interactBird(player, itemStack);
            }
            if (itemStack.isOf(Items.COOKIE)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
                if (player.isCreative() || !this.isInvulnerable()) {
                    this.damage(DamageSource.player(player), Float.MAX_VALUE);
                }
            }
            ActionResult actionResult = itemStack.useOnEntity(player, this, hand);
            if (actionResult.isAccepted()) {
                return actionResult;
            }
            if (!this.isTame()) {
                this.playAngrySound();
                return ActionResult.success(this.world.isClient);
            }
            boolean bl2 = bl = !this.isBaby() && !this.isSaddled() && itemStack.isOf(Items.SADDLE);
            if (this.isHorseArmor(itemStack) || bl) {
                this.openInventory(player);
                return ActionResult.success(this.world.isClient);
            }
        }
        this.putPlayerOnBack(player);
        return ActionResult.success(this.world.isClient);
    }
    public ActionResult interactBird(PlayerEntity player, ItemStack stack) {
        boolean bl = this.receiveFood(player, stack);
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        if (this.world.isClient) {
            return ActionResult.CONSUME;
        }
        return bl ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
        this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.2));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new TemptGoal(this, 1.25, Ingredient.ofItems(Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE), false));
    }
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        BlockPos blockPos = this.getBlockPos();
        Biome biome = world.getBiome(blockPos).value();
        this.setVariant(biome.isCold(blockPos)? 5 : biome.isHot(blockPos)?6: this.random.nextInt(5));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, 0.5f * this.getStandingEyeHeight(), this.getWidth() * 0.2f);
    }
    public double getMountedHeightOffset() {
        return this.getDimensions(this.getPose()).height * 0.4;
    }
    @Override
    public void updatePassengerPosition(Entity passenger) {
        super.updatePassengerPosition(passenger);
        if (passenger instanceof MobEntity mobEntity) {
            this.bodyYaw = mobEntity.bodyYaw;
        }
        float mobEntity = MathHelper.sin(this.bodyYaw * ((float) Math.PI / 180));
        float f = MathHelper.cos(this.bodyYaw * ((float) Math.PI / 180));
        float g = 0.5f;
        passenger.setPosition(this.getX() + (double) (g * mobEntity), this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset(), this.getZ() - (double) (g * f));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).bodyYaw = this.bodyYaw;
        }
    }

    public void tickMovement() {
        super.tickMovement();
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation = (float)((double)this.maxWingDeviation + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.onGround && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
        }

        this.flapSpeed = (float)((double)this.flapSpeed * 0.9D);
        Vec3d vec3d = this.getVelocity();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flapProgress += this.flapSpeed * 2.0F;

        if (!this.world.isClient && this.isAlive() && !this.isBaby() && --this.featherDropTime <= 0) {
            for (int i = 0; i < this.random.nextInt(2)+1 ; i++) {
                this.dropItem(Declarar.MEGA_FEATHER);
            }
            this.featherDropTime = this.random.nextInt(6000) + 6000;
        }
    }
    @Override
    protected boolean hasWings() {
        return this.speed > this.flapProgress;
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f);
        this.flapProgress = this.speed + this.maxWingDeviation / 2.0f;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance > 1.0f) {
            this.playSound(SoundEvents.ENTITY_GOAT_STEP, 0.4f, 1.0f);
        }
        return false;
    }

    protected boolean receiveFood(PlayerEntity player, ItemStack item) {
        boolean bl = false;
        float health = 0.0f;
        int temper = 0;
        if (Arrays.stream(BREEDING_INGREDIENT).anyMatch((item::isOf))) {
            health = 2f;
            temper = 3;
        } else if (item.isOf(Items.GOLDEN_CARROT)) {
            health = 4.0f;
            temper = 5;
            if (!this.world.isClient && this.isTame() && this.getBreedingAge() == 0 && !this.isInLove()) {
                bl = true;
                this.lovePlayer(player);
            }
        } else if (item.isOf(Items.GOLDEN_APPLE) || item.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            health = 10.0f;
            temper = 10;
            if (!this.world.isClient && this.isTame() && this.getBreedingAge() == 0 && !this.isInLove()) {
                bl = true;
                this.lovePlayer(player);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && health > 0.0f) {
            this.heal(health);
            bl = true;
        }
        if (temper > 0 && (bl || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            bl = true;
            if (!this.world.isClient) {
                this.addTemper(temper);
            }
        }
        if (bl) {
            this.playEatingAnimation();
            this.emitGameEvent(GameEvent.EAT, this);
        }
        return bl;
    }
    private void playEatingAnimation() {
        SoundEvent soundEvent;
        ((HorseBaseEntityAccessor)this).invokeSetEating();
        this.eatingTicks = 10;
        if (!this.isSilent() && (soundEvent = this.getEatSound()) != null) {
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundCategory(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return (Arrays.stream(BREEDING_INGREDIENT).anyMatch(stack::isOf));
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        ++this.soundTicks;
        if (this.soundTicks > 5) {
            this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.30f, 0f);
        }
    }
    public float getSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f - 1.0f;
    }
    @Override
    protected void playWalkSound(BlockSoundGroup group) {
        super.playWalkSound(group);
            this.playSound(SoundEvents.ENTITY_PARROT_STEP, group.getVolume() * 0.6f, group.getPitch());
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound() {
        return ParrotEntity.getRandomSound(this.world, this.world.random);
    }

    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Override
    @Nullable
    protected SoundEvent getEatSound() {
        return SoundEvents.ENTITY_PARROT_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        super.getHurtSound(source);
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_PARROT_IMITATE_ENDERMITE;
    }

    @Override
    public void tick() {
        if (eatingTicks>0)
            eatingTicks--;

        super.tick();
    }
    @Override
    public boolean hasArmorSlot() {
        return true;
    }

    @Override
    public boolean isHorseArmor(ItemStack item) {
        return item.getItem() instanceof HorseArmorItem;
    }
    public ItemStack getArmorType() {
        return this.getEquippedStack(EquipmentSlot.CHEST);
    }
    private void equipArmor(ItemStack stack) {
        this.equipStack(EquipmentSlot.CHEST, stack);
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
    }
    @Override
    protected void updateSaddle() {
        if (this.world.isClient) {
            return;
        }
        super.updateSaddle();
        this.setArmorTypeFromStack(this.items.getStack(1));
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    private void setArmorTypeFromStack(ItemStack stack) {
        this.equipArmor(stack);
        if (!this.world.isClient) {
            int i;
            this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).removeModifier(HORSE_ARMOR_BONUS_ID);
            if (this.isHorseArmor(stack) && (i = ((HorseArmorItem)stack.getItem()).getBonus()) != 0) {
                this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).addTemporaryModifier(new EntityAttributeModifier(HORSE_ARMOR_BONUS_ID, "Horse armor bonus", (double)i, EntityAttributeModifier.Operation.ADDITION));
            }
        }
    }
    //gecko
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "locomotion_controller", 5, this::locomotion_predicate));
        animationData.addAnimationController(new AnimationController<>(this, "flutter_controller", 5, this::flutter_predicate));
        animationData.addAnimationController(new AnimationController<>(this, "eating_controller", 0, this::eating_predicate));
    }

    private <E extends IAnimatable> PlayState locomotion_predicate(AnimationEvent<E> event) {
        MegaParrotEntity megaParrot = (MegaParrotEntity) event.getAnimatable();

        if (event.isMoving()) {
            Vec3d vec3d = megaParrot.getVelocity().normalize();
            if (vec3d.x > 0.5f || vec3d.x < -0.5f || vec3d.z > 0.5f || vec3d.z < -0.5f)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.run", true));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.walk", true));
        } else
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.idle", true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState flutter_predicate(AnimationEvent<E> event)
    {
        MegaParrotEntity megaParrot = (MegaParrotEntity) event.getAnimatable();
//        Logger.getGlobal().log(Level.SEVERE,megaParrot.dataTracker.get(FALLING)>0?megaParrot.dataTracker.get(FALLING)+"":"");

        if (megaParrot.fallDistance>0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.wing_flutter", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.wing_flutter",false));
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState eating_predicate(AnimationEvent<E> event)
    {
        if (this.eatingTicks>0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.eat", false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.wing_flutter", false));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

}
