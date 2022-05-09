package website.skylorbeck.minecraft.megaparrot.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

import java.util.UUID;

public class BirdWhistle extends Item {
    public BirdWhistle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && itemStack.isOf(Declarar.BIRD_WHISTLE)){
            NbtCompound nbtCompound = itemStack.getOrCreateNbt();
            if (nbtCompound!=null && nbtCompound.contains("bird")){
                BlockPos pos = user.getBlockPos();
                user.playSound(SoundEvents.ENTITY_DOLPHIN_PLAY,SoundCategory.PLAYERS,1f,0.5f);
                Entity bird = ((ServerWorld)world).getEntity(nbtCompound.getUuid("bird"));
                if (bird!=null) {
                    bird.teleport(pos.getX(), pos.getY()+1, pos.getZ());
                    user.sendMessage(Text.of("Bird summoned"),true);
                } else {
                    user.sendMessage(Text.of("Your bird was unable to hear you"),true);
                }
            } else {
                user.sendMessage(Text.of("No bird is attuned to this whistle"),true);
            }
            user.getItemCooldownManager().set(this,100);
        }
        return super.use(world, user, hand);
    }


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity.getType().equals(Declarar.MEGA_PARROT_ENTITY_TYPE)) {
            MegaParrotEntity mpe = (MegaParrotEntity) entity;
            UUID uuid = mpe.getOwnerUuid();
            if (uuid!=null&& uuid.equals(user.getUuid())) {
                NbtCompound nbtCompound = stack.getOrCreateNbt();
                nbtCompound.putUuid("bird", entity.getUuid());
                stack.writeNbt(nbtCompound);
                user.sendMessage(Text.of("Your Parrot has attuned to this whistle"), true);
            } else {
                user.sendMessage(Text.of("This is not your Bird"),true);
            }
        }

        return super.useOnEntity(stack, user, entity, hand);
    }
}
