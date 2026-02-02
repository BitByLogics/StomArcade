package net.bitbylogic.stomarcade.util.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.MetadataDef;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class TextDisplayBuilder {

    public static int TRANSPARENT_COLOR = 0x00000000;

    private Component text = Component.empty();

    private int lineWidth = MetadataDef.TextDisplay.LINE_WIDTH.defaultValue();
    private int backgroundColor = MetadataDef.TextDisplay.BACKGROUND_COLOR.defaultValue();
    private int brightness = MetadataDef.Display.BRIGHTNESS_OVERRIDE.defaultValue();
    private byte textOpacity = MetadataDef.TextDisplay.TEXT_OPACITY.defaultValue();

    private boolean shadow = MetadataDef.TextDisplay.HAS_SHADOW.defaultValue();
    private boolean seeThrough = MetadataDef.TextDisplay.IS_SEE_THROUGH.defaultValue();

    private TextDisplayMeta.Alignment alignment = TextDisplayMeta.Alignment.values()[MetadataDef.TextDisplay.ALIGNMENT.defaultValue()];

    private AbstractDisplayMeta.BillboardConstraints billboard = AbstractDisplayMeta.BillboardConstraints.values()[MetadataDef.Display.BILLBOARD_CONSTRAINTS.defaultValue()];

    private Vec scale = MetadataDef.Display.SCALE.defaultValue().asVec();
    private Point translation = MetadataDef.Display.TRANSLATION.defaultValue();

    private TextDisplayBuilder() {}

    public static TextDisplayBuilder empty() {
        return new TextDisplayBuilder();
    }

    public static TextDisplayBuilder fancy() {
        return new TextDisplayBuilder()
                .shadow(true)
                .brightness(15, 15)
                .backgroundColor(TRANSPARENT_COLOR)
                .alignment(TextDisplayMeta.Alignment.CENTER)
                .billboard(AbstractDisplayMeta.BillboardConstraints.CENTER);
    }

    public int lineWidth() {
        return lineWidth;
    }

    public Component text() {
        return text;
    }

    public int backgroundColor() {
        return backgroundColor;
    }

    public int brightness() {
        return brightness;
    }

    public byte textOpacity() {
        return textOpacity;
    }

    public boolean shadow() {
        return shadow;
    }

    public boolean seeThrough() {
        return seeThrough;
    }

    public TextDisplayMeta.Alignment alignment() {
        return alignment;
    }

    public AbstractDisplayMeta.BillboardConstraints billboard() {
        return billboard;
    }

    public Vec scale() {
        return scale;
    }

    public Point translation() {
        return translation;
    }

    public TextDisplayBuilder text(@NotNull Component text) {
        this.text = text;
        return this;
    }

    public TextDisplayBuilder lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public TextDisplayBuilder backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextDisplayBuilder brightness(int blockLight, int skyLight) {
        this.brightness = (blockLight & 0xF) << 4 | (skyLight & 0xF) << 20;
        return this;
    }

    public TextDisplayBuilder textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

    public TextDisplayBuilder shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public TextDisplayBuilder seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    public TextDisplayBuilder alignment(@NotNull TextDisplayMeta.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextDisplayBuilder billboard(@NotNull AbstractDisplayMeta.BillboardConstraints billboard) {
        this.billboard = billboard;
        return this;
    }

    public TextDisplayBuilder scale(@NotNull Vec scale) {
        this.scale = scale;
        return this;
    }

    public TextDisplayBuilder translation(@NotNull Point translation) {
        this.translation = translation;
        return this;
    }

    public Entity build(@NotNull Instance instance, @NotNull Pos position) {
        Entity textDisplay = new Entity(EntityType.TEXT_DISPLAY);
        textDisplay.setNoGravity(true);

        TextDisplayMeta textDisplayMeta = (TextDisplayMeta) textDisplay.getEntityMeta();
        textDisplayMeta.setText(text);
        textDisplayMeta.setLineWidth(lineWidth);
        textDisplayMeta.setBackgroundColor(backgroundColor);
        textDisplayMeta.setBrightnessOverride(brightness);
        textDisplayMeta.setTextOpacity(textOpacity);
        textDisplayMeta.setShadow(shadow);
        textDisplayMeta.setSeeThrough(seeThrough);
        textDisplayMeta.setAlignment(alignment);
        textDisplayMeta.setBillboardRenderConstraints(billboard);

        if (scale != null) {
            textDisplayMeta.setScale(scale);
        }

        if (translation != null) {
            textDisplayMeta.setTranslation(translation);
        }

        textDisplay.setInstance(instance, position);

        return textDisplay;
    }

}
