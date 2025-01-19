package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.gametest.block.BlockStateExp;
import ca.fxco.gametestlib.gametest.block.BlockStateSuggestions;
import ca.fxco.gametestlib.network.GameTestNetwork;
import ca.fxco.gametestlib.network.packets.ServerboundSetCheckStatePacket;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CheckStateScreen extends Screen {

    private Direction initialDirection;
    private boolean initialFailOnFound;

    private final CheckStateBlockEntity blockEntity;
    private EditBox tickEdit;
    private CycleButton<Direction> directionCycleButton;
    private EditBox stateEdit;
    private BlockStateSuggestions blockStateSuggestions;
    private Checkbox failOnFoundCheckbox;

    protected CheckStateScreen(CheckStateBlockEntity blockEntity) {
        super(Component.translatable("screen.gametestlib.check_state_block.title"));
        this.blockEntity = blockEntity;
    }

    private void onDone() {
        Direction newDirection = this.blockEntity.getDirection();
        HolderLookup<Block> holderLookup = BuiltInRegistries.BLOCK.asLookup();
        try {
            String stateValue = this.stateEdit.getValue();
            BlockState state = BlockStateParser.parseForBlock(holderLookup, stateValue, true).blockState();
            // Has properties, matches specific state. - Has no properties, only matches block
            BlockStateExp blockStateExp = BlockStateExp.of(state, stateValue.indexOf('[') == -1);
            ServerboundSetCheckStatePacket packet = new ServerboundSetCheckStatePacket(
                    this.blockEntity.getBlockPos(),
                    Integer.parseInt(this.tickEdit.getValue()),
                    this.blockEntity.isFailOnFound(),
                    newDirection,
                    blockStateExp
            );
            GameTestNetwork.sendToServer(packet);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        } // Just don't send if validation failed
        this.minecraft.setScreen(null);
    }

    public void onCancel() {
        this.blockEntity.setDirection(initialDirection);
        this.blockEntity.setFailOnFound(initialFailOnFound);
        this.minecraft.setScreen(null);
    }

    @Override
    public void onClose() {
        onCancel();
    }

    @Override
    protected void init() {
        this.initialDirection = this.blockEntity.getDirection();
        this.initialFailOnFound = this.blockEntity.isFailOnFound();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onDone();
        }).bounds(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
            onCancel();
        }).bounds(this.width / 2 + 4, 210, 150, 20).build());

        this.tickEdit = new EditBox(this.font, this.width / 2 - 150 - 4, 50, 150, 20, Component.translatable("screen.gametestlib.check_state_block.tick"));
        this.tickEdit.setMaxLength(6);
        this.tickEdit.setValue("" + this.blockEntity.getTick());
        this.addWidget(this.tickEdit);
        //this.font, this.width / 2 + 4, 50, 150, 20, Component.translatable("screen.gametestlib.pulse_state_block.duration")
        this.directionCycleButton = new CycleButton.Builder<Direction>(dir -> Component.literal(dir.toString()))
                .withInitialValue(this.initialDirection)
                .withValues(Direction.values())
                .create(this.width / 2 + 4, 50, 150, 20, Component.translatable("screen.gametestlib.check_state_block.direction"), (cycleButton, dir) -> {
                    this.blockEntity.setDirection(dir);
                });
        this.addWidget(this.directionCycleButton);
        this.stateEdit = new EditBox(this.font, this.width / 2 - 154, 90, 308, 20, Component.translatable("screen.gametestlib.check_state_block.state"));
        this.stateEdit.setMaxLength(255);
        this.stateEdit.setValue(this.blockEntity.getBlockStateExp().asString());
        this.addWidget(this.stateEdit);
        this.failOnFoundCheckbox = Checkbox.builder(
                Component.translatable("screen.gametestlib.check_state_block.failOnFind"), minecraft.font)
                .selected(this.blockEntity.isFailOnFound()).pos(this.width / 2 - 154, 130)
                .onValueChange((checkbox, value) -> this.blockEntity.setFailOnFound(value))
                .build();
        this.addWidget(this.failOnFoundCheckbox);

        this.setInitialFocus(this.stateEdit);

        this.blockStateSuggestions = new BlockStateSuggestions(this.minecraft, this, this.stateEdit, this.font, true, false, 0, 7, false, Integer.MIN_VALUE);
        this.blockStateSuggestions.setAllowSuggestions(true);
        this.blockStateSuggestions.updateCommandInfo();

        this.stateEdit.setResponder(str -> this.blockStateSuggestions.updateCommandInfo());
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (this.blockStateSuggestions.keyPressed(i, j, k)) {
            return true;
        } else if (super.keyPressed(i, j, k)) {
            return true;
        } else if (i != 257 && i != 335) {
            return false;
        }
        this.onDone();
        return true;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return this.blockStateSuggestions.mouseScrolled(f) || super.mouseScrolled(d, e, f, g);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return this.blockStateSuggestions.mouseClicked(d, e, i) || super.mouseClicked(d, e, i);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 16777215);

        guiGraphics.drawString(this.font, Component.translatable("screen.gametestlib.check_state_block.tick"), this.width / 2 - 153, 40, 10526880);
        this.tickEdit.render(guiGraphics, i, j, f);
        guiGraphics.drawString(this.font, Component.translatable("screen.gametestlib.check_state_block.direction"), this.width / 2 + 4, 40, 10526880);
        this.directionCycleButton.render(guiGraphics, i, j, f);
        guiGraphics.drawString(this.font, Component.translatable("screen.gametestlib.check_state_block.state"), this.width / 2 - 153, 80, 10526880);
        this.stateEdit.render(guiGraphics, i, j, f);
        this.failOnFoundCheckbox.render(guiGraphics, i, j, f);

        this.blockStateSuggestions.render(guiGraphics, i, j);
    }
}
