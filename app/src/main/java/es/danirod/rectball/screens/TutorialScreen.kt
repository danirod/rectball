/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import es.danirod.rectball.Constants
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.android.settings.SettingsManager
import es.danirod.rectball.model.BallColor
import es.danirod.rectball.model.Bounds
import es.danirod.rectball.model.CombinationFinder
import es.danirod.rectball.scene2d.game.BallActor
import es.danirod.rectball.scene2d.game.BoardActor
import es.danirod.rectball.scene2d.game.Hud
import es.danirod.rectball.scene2d.listeners.DefaultBallSelectionListener
import es.danirod.rectball.scene2d.ui.ConfirmDialog
import es.danirod.rectball.scene2d.ui.MessageDialog

class TutorialScreen(game: RectballGame) : AbstractScreen(game) {

    private lateinit var hud: Hud

    private lateinit var board: BoardActor

    private var currentModal: MessageDialog? = null

    private val continueText = game.locale["core.continue"]
    private val okText = game.locale["core.ok"]

    /* The different modals in use in this tutorial. */
    private lateinit var modalWelcome: MessageDialog
    private lateinit var modalThisIsBoard: MessageDialog
    private lateinit var modalThisIsTimer: MessageDialog
    private lateinit var modalThisIsScore: MessageDialog
    private lateinit var modalThePointOfGame: MessageDialog
    private lateinit var modalWhatToDo: MessageDialog
    private lateinit var modalThatWasEasy: MessageDialog
    private lateinit var modalBeatAHarderOne: MessageDialog
    private lateinit var modalYouGotThis: MessageDialog

    /* Assets used for the hand animation. */
    private lateinit var hand: Image
    private lateinit var handNormalDrawable: TextureRegionDrawable
    private lateinit var handHoverDrawable: TextureRegionDrawable

    override fun setUpInterface(table: Table) {
        /* Hand assets. */
        handNormalDrawable = TextureRegionDrawable(game.manager.get("hand-normal.png", Texture::class.java))
        handHoverDrawable = TextureRegionDrawable(game.manager.get("hand-hover.png", Texture::class.java))
        hand = Image(handNormalDrawable)

        table.setFillParent(true)

        hud = Hud(game).apply {
            help.isVisible = false
            pause.isVisible = false
            timer.isVisible = false
            score.isVisible = false
        }

        board = BoardActor(game.ballAtlas, game.appSkin, game.state.board).apply {
            touchable = Touchable.disabled
            isTransform = true
            isVisible = false
        }

        table.add(hud).growX().align(Align.top).row()
        table.add(board).growX().expand().height(Value.percentWidth(1f)).align(
            Align.center
        ).row()
        board.pack()
        table.pack()

        modalWelcome = makeModal(tutorialStrings[0], continueText) {
            setVisibility(board = true, score = false, timer = false)
            showModal(modalThisIsBoard, Align.top)
        }

        modalThisIsBoard = makeModal(tutorialStrings[1], continueText) {
            setVisibility(board = true, score = false, timer = true)
            showModal(modalThisIsTimer, Align.bottom)

            /* Start the timer. */
            hud.timer.isRunning = true
        }

        modalThisIsTimer = makeModal(tutorialStrings[2], continueText) {
            /* Stop the timer. */
            hud.timer.isRunning = false
            hud.timer.seconds = Constants.SECONDS.toFloat()

            setVisibility(board = true, score = true, timer = true)

            /* Make the score presented by the score actor change every 100 ms. */
            hud.score.addAction(Actions.forever(Actions.delay(0.1f, Actions.run {
                hud.score.value += MathUtils.random(10000)
                hud.score.value %= 10000
            })))

            showModal(modalThisIsScore, Align.bottom)
        }

        modalThisIsScore = makeModal(tutorialStrings[3], continueText) {
            /* Stop the score. */
            hud.score.clearActions()
            hud.score.value = 0

            setBoardColors(listOf(
                    "YBRYBY",
                    "RGYGYR",
                    "YYRRBB",
                    "RGRRBY",
                    "YRBRYR",
                    "BRRYRB",
            ))
            showModal(modalThePointOfGame, Align.center)
        }

        modalThePointOfGame = makeModal(tutorialStrings[4], continueText) {
            showModal(modalWhatToDo, Align.top)

            hand.apply {
                drawable = handNormalDrawable
                layout()
                setPosition(Constants.VIEWPORT_WIDTH.toFloat(), 0f)
                setScale(0.75f)
            }

            val oneCorner = board.getBall(2, 2)
                .let { b -> Vector2(b.getX(Align.center), b.getY(Align.center))
                .apply { board.localToStageCoordinates(this) }
                    .apply { sub(38f * 0.75f, 171f * 0.75f) }
            }
            val otherCorner = board.getBall(3, 3)
                .let { b -> Vector2(b.getX(Align.center), b.getY(Align.center))
                .apply { board.localToStageCoordinates(this) }
                .apply { sub(38f * 0.75f, 171f * 0.75f) }
            }

            hand.addAction(Actions.forever(
                    Actions.sequence(
                            Actions.moveTo(oneCorner.x, oneCorner.y, 0.5f),
                            Actions.delay(0.25f),
                            Actions.run { hand.drawable = handHoverDrawable },
                            Actions.delay(0.25f),
                            Actions.moveTo(otherCorner.x, otherCorner.y, 1f),
                            Actions.delay(0.25f),
                            Actions.run { hand.drawable = handNormalDrawable },
                            Actions.delay(0.25f),
                    )
            ))
            stage.addActor(hand)
        }

        modalWhatToDo = makeModal(tutorialStrings[5], okText) {
            /* Remove the hand. */
            hand.clearActions()
            hand.addAction(Actions.sequence(
                    Actions.run { hand.drawable = handNormalDrawable },
                    Actions.moveTo(Constants.VIEWPORT_WIDTH.toFloat(), 0f, 0.5f),
                    Actions.removeActor(),
            ))

            board.touchable = Touchable.enabled
            board.addSubscriber(object : DefaultBallSelectionListener() {
                override fun onSelectionSucceeded(selection: MutableList<BallActor>?) {
                    stopCombinationHelpAnimation()

                    rewardPlayer(selection!!)

                    /* Next step */
                    showModal(modalThatWasEasy, Align.center)
                    board.clearSubscribers()
                    setBoardColors(listOf(
                            "YBRYBY",
                            "RGYGYR",
                            "YYRBBB",
                            "RGYGBY",
                            "YRBRYR",
                            "BRRYRB",
                    ))
                }
            })

            startCombinationHelpAnimation()
        }

        modalThatWasEasy = makeModal(tutorialStrings[6], okText) {
            board.addSubscriber(object : DefaultBallSelectionListener() {
                override fun onSelectionSucceeded(selection: MutableList<BallActor>?) {
                    stopCombinationHelpAnimation()

                    rewardPlayer(selection!!)

                    /* Next step */
                    showModal(modalBeatAHarderOne, Align.center)
                    board.clearSubscribers()
                    setBoardColors(listOf(
                            "YBRYBY",
                            "RBYGYR",
                            "YBGGBB",
                            "RYGRBY",
                            "YRBRYR",
                            "BRRYRB",
                    ))
                }
            })

            startCombinationHelpAnimation()
        }

        modalBeatAHarderOne = makeModal(tutorialStrings[7], okText) {
            board.addSubscriber(object : DefaultBallSelectionListener() {
                override fun onSelectionSucceeded(selection: MutableList<BallActor>?) {
                    stopCombinationHelpAnimation()

                    rewardPlayer(selection!!)

                    /* Next step. */
                    showModal(modalYouGotThis, Align.center)
                    board.clearSubscribers()
                }
            })

            startCombinationHelpAnimation()
        }

        modalYouGotThis = makeModal(tutorialStrings[8], okText) {
            exitTutorial()
        }
    }

    override fun show() {
        super.show()
        showModal(modalWelcome, Align.center)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
            showLeaveConfirmationDialog(stage)
        }
        super.render(delta)
    }

    override fun hide() {
        /* Hide the modal if there is a dialog currently being presented. */
        currentModal?.hide(Actions.hide())

        /* Hide any effect currently playing. */
        hand.remove()
        stopCombinationHelpAnimation()

        super.hide()
    }


    private val watchdogTask: Timer.Task = object : Timer.Task() {
        override fun run() {
            val finder = CombinationFinder.create(game.state.board)
            finder.possibleBounds.forEach {
                board.addAction(board.shake(it, 10f, 5, 0.1f))
            }
        }
    }

    private fun startCombinationHelpAnimation() {
        if (!watchdogTask.isScheduled) {
            Timer.schedule(watchdogTask, 8f, 4f, -2)
        }
    }

    private fun stopCombinationHelpAnimation() {
        if (watchdogTask.isScheduled) {
            watchdogTask.cancel()
        }
    }

    private fun exitTutorial() {
        /* Disable the board. */
        board.setColoured(false)
        board.touchable = Touchable.disabled

        /* Animate to game over. */
        board.addAction(Actions.delay(0.5f, board.hideBoard()))
        hud.addAction(Actions.delay(0.5f, Actions.fadeOut(0.25f)))

        /* Store in the settings the fact that we made the tutorial and leave. */
        stage.addAction(Actions.delay(1.5f, Actions.run {
            val editor = game.context.settings.preferences.edit()
            editor.putBoolean(SettingsManager.TAG_ASKED_TUTORIAL, true)
            editor.putBoolean(SettingsManager.TAG_NEW_SELECTION_MODE_NOTIFIED, true)
            editor.apply()
            game.popScreen()
        }))
    }

    private fun showLeaveConfirmationDialog(stage: Stage) {
        ConfirmDialog(game.appSkin, game.locale["tutorial.cancel"], game.locale["core.yes"], game.locale["core.no"]).apply {
            setCallback(object : ConfirmDialog.ConfirmCallback {
                override fun ok() {
                    game.player.playSound(SoundPlayer.SoundCode.SUCCESS)

                    /* Stop any animations. */
                    hand.remove()
                    stopCombinationHelpAnimation()

                    /* Remove any visible modals. */
                    listOf(
                            modalWelcome, modalThisIsBoard, modalThisIsTimer, modalThisIsScore, modalThePointOfGame,
                            modalWhatToDo, modalThatWasEasy, modalBeatAHarderOne, modalYouGotThis
                    ).forEach { it.hide(null) }

                    val modal = makeModal(game.locale["main.dismiss_tutorial"], game.locale["core.ok"]) {
                        game.player.playSound(SoundPlayer.SoundCode.SUCCESS)

                        exitTutorial()
                    }
                    showModal(modal, Align.center)
                }

                override fun cancel() {
                    game.player.playSound(SoundPlayer.SoundCode.FAIL)
                }
            })
            show(stage)
        }
    }

    private fun showModal(modal: MessageDialog, alignment: Int) {
        val fadeAction = Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.1f))
        modal.show(stage, fadeAction)
        currentModal = modal

        /* Position the modal depending on the given alignment. */
        val modalX = (stage.width - modal.width) / 2
        val modalY = when {
            (alignment and Align.top) != 0 -> Constants.VIEWPORT_HEIGHT - modal.height - 20f
            (alignment and Align.bottom) != 0 -> 20f
            else -> (stage.height - modal.height) / 2
        }
        modal.setPosition(modalX, modalY)
    }

    private fun rewardPlayer(selection: List<BallActor>) {
        /* Get the bounds. */
        val balls = selection.map { it.ball }
        val bounds = Bounds.fromBallList(balls)

        /* Add score. */
        val score = bounds.rows() * bounds.cols()
        hud.score.value += score
        stage.addActor(board.showPartialScore(score, bounds, false, false))

        /* Add sound. */
        game.player.playSound(SoundPlayer.SoundCode.SUCCESS)
        game.haptics.vibrateMilliseconds(60)
    }

    private fun setBoardColors(values: List<String>) {
        values.asReversed().forEachIndexed { y, row ->
            row.toCharArray().forEachIndexed { x, char ->
                val color = when (char) {
                    'Y' -> BallColor.YELLOW
                    'R' -> BallColor.RED
                    'B' -> BallColor.BLUE
                    'G' -> BallColor.GREEN
                    else -> null
                }
                board.getBall(x, y).ball.color = color
            }
        }
        board.setColoured(true)
    }

    /* Dirty, but this is just so that Android Studio doesn't see these strings as unused. */
    private val tutorialStrings = listOf(
        game.locale["tutorial.guide.0"],
        game.locale["tutorial.guide.1"],
        game.locale["tutorial.guide.2"],
        game.locale["tutorial.guide.3"],
        game.locale["tutorial.guide.4"],
        game.locale["tutorial.guide.5"],
        game.locale["tutorial.guide.6"],
        game.locale["tutorial.guide.7"],
        game.locale["tutorial.guide.8"],
    )

    private fun makeModal(text: String, buttonText: String, onDismiss: () -> Unit): MessageDialog =
            MessageDialog(game.appSkin, text, buttonText).apply {
                setCallback {
                    game.player.playSound(SoundPlayer.SoundCode.SUCCESS)
                    onDismiss()
                }
            }

    private fun setVisibility(board: Boolean, score: Boolean, timer: Boolean) {
        this.board.isVisible = board
        this.hud.score.isVisible = score
        this.hud.timer.isVisible = timer
    }
}
