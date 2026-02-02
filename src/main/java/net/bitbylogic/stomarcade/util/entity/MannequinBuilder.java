package net.bitbylogic.stomarcade.util.entity;

import net.bitbylogic.stomarcade.util.ServerUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.player.ResolvableProfile;
import org.jetbrains.annotations.NotNull;

public class MannequinBuilder {

    private ResolvableProfile profile = MetadataDef.Mannequin.PROFILE.defaultValue();

    private boolean immovable = MetadataDef.Mannequin.IMMOVABLE.defaultValue();
    private boolean noGravity = MetadataDef.LivingEntity.HAS_NO_GRAVITY.defaultValue();
    private boolean customNameVisible = MetadataDef.LivingEntity.CUSTOM_NAME_VISIBLE.defaultValue();
    private boolean collision = true;

    private Component description = MetadataDef.Mannequin.DESCRIPTION.defaultValue();

    private double scale = Attribute.SCALE.defaultValue();

    private MannequinBuilder() {

    }

    public static MannequinBuilder empty() {
        return new MannequinBuilder();
    }

    public static MannequinBuilder npc(@NotNull PlayerSkin playerSkin) {
        return npc(new ResolvableProfile(playerSkin));
    }

    public static MannequinBuilder npc() {
        return npc(ResolvableProfile.EMPTY);
    }

    public static MannequinBuilder npc(@NotNull ResolvableProfile profile) {
        return new MannequinBuilder()
                .profile(profile)
                .immovable(true)
                .setNoGravity(true)
                .setCustomNameVisible(false)
                .description(Component.empty())
                .collision(false);
    }

    public ResolvableProfile profile() {
        return profile;
    }

    public boolean immovable() {
        return immovable;
    }

    public boolean noGravity() {
        return noGravity;
    }

    public boolean customNameVisible() {
        return customNameVisible;
    }

    public boolean collision() {
        return collision;
    }

    public Component description() {
        return description;
    }

    public double scale() {
        return scale;
    }

    public MannequinBuilder profile(@NotNull ResolvableProfile profile) {
        this.profile = profile;
        return this;
    }

    public MannequinBuilder immovable(boolean immovable) {
        this.immovable = immovable;
        return this;
    }

    public MannequinBuilder setNoGravity(boolean hasNoGravity) {
        this.noGravity = hasNoGravity;
        return this;
    }

    public MannequinBuilder setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
        return this;
    }

    public MannequinBuilder collision(boolean collision) {
        this.collision = collision;
        return this;
    }

    public MannequinBuilder description(@NotNull Component description) {
        this.description = description;
        return this;
    }

    public MannequinBuilder scale(double scale) {
        this.scale = scale;
        return this;
    }

    public Entity build(@NotNull Instance instance, @NotNull Pos position) {
        LivingEntity mannequin = new LivingEntity(EntityType.MANNEQUIN);
        mannequin.setNoGravity(noGravity);
        mannequin.getAttribute(Attribute.SCALE).setBaseValue(scale);
        mannequin.setCustomNameVisible(customNameVisible);

        MannequinMeta mannequinMeta = (MannequinMeta) mannequin.getEntityMeta();

        mannequinMeta.setProfile(profile);
        mannequinMeta.setImmovable(immovable);
        mannequinMeta.setDescription(description);

        mannequin.setInstance(instance, position);

        if (!collision) {
            ServerUtil.preventCollision(mannequin);
        }

        return mannequin;
    }

}
