package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var vibrator: Vibrator

    private lateinit var gameArea: FrameLayout
    private lateinit var carImage: ImageView
    private lateinit var leftButton: Button
    private lateinit var rightButton: Button
    private lateinit var life1Image: ImageView
    private lateinit var life2Image: ImageView
    private lateinit var life3Image: ImageView

    private val lanePositions = FloatArray(3)
    private var currentLane = 1
    private var lives = 3
    private var gameRunning = false

    private val handler = Handler(Looper.getMainLooper())
    private val obstacleList = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        gameArea = findViewById(R.id.gameArea)
        carImage = findViewById(R.id.carView)
        leftButton = findViewById(R.id.buttonLeft)
        rightButton = findViewById(R.id.buttonRight)
        life1Image = findViewById(R.id.heart1)
        life2Image = findViewById(R.id.heart2)
        life3Image = findViewById(R.id.heart3)

        initLanes()
        initButtons()
        startGame()
    }

    private fun initLanes() {
        gameArea.post {
            val w = gameArea.width.toFloat()
            lanePositions[0] = w * 0.2f
            lanePositions[1] = w * 0.5f
            lanePositions[2] = w * 0.8f
            moveCarToLane(1)
        }
    }

    private fun initButtons() {
        leftButton.setOnClickListener {
            if (!gameRunning) return@setOnClickListener
            if (currentLane > 0) {
                currentLane -= 1
                moveCarToLane(currentLane)
            }
        }

        rightButton.setOnClickListener {
            if (!gameRunning) return@setOnClickListener
            if (currentLane < 2) {
                currentLane += 1


                moveCarToLane(currentLane)
            }
        }
    }

    private fun moveCarToLane(lane: Int) {
        val targetX = lanePositions[lane] - carImage.width / 2f
        carImage.x = targetX
    }

    private fun startGame() {
        lives = 3
        updateLivesImages()
        clearAllObstacles()
        gameRunning = true
        handler.post(spawnObstacleRunnable)
        handler.post(gameLoopRunnable)
    }

    private val spawnObstacleRunnable = object : Runnable {
        override fun run() {
            if (!gameRunning) return
            createObstacle()
            handler.postDelayed(this, 800)
        }
    }

    private fun createObstacle() {
        if (gameArea.height == 0) return

        val lane = Random.nextInt(0, 3)
        val size = 140

        val obstacle = ImageView(this)
        obstacle.setImageResource(R.drawable.bird)
        val params = FrameLayout.LayoutParams(size, size)
        obstacle.layoutParams = params

        gameArea.addView(obstacle)

        obstacle.x = lanePositions[lane] - size / 2f
        obstacle.y = -size.toFloat()

        obstacleList.add(obstacle)
    }

    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            if (!gameRunning) return

            moveObstaclesDown()

            if (checkCollision()) {
                handleCrash()
            }

            handler.postDelayed(this, 20)
        }
    }

    private fun moveObstaclesDown() {
        val speed = 15f
        val iterator = obstacleList.iterator()

        while (iterator.hasNext()) {
            val o = iterator.next()
            o.y += speed

            if (o.y > gameArea.height) {
                gameArea.removeView(o)
                iterator.remove()
            }
        }
    }

    private fun checkCollision(): Boolean {
        val carLeft = carImage.x
        val carTop = carImage.y
        val carRight = carLeft + carImage.width
        val carBottom = carTop + carImage.height

        obstacleList.forEach { o ->
            val oLeft = o.x
            val oTop = o.y
            val oRight = oLeft + o.width
            val oBottom = oTop + o.height

            val intersect =
                carLeft < oRight &&
                        carRight > oLeft &&
                        carTop < oBottom &&
                        carBottom > oTop

            if (intersect) return true
        }
        return false
    }

    private fun handleCrash() {
        clearAllObstacles()
        lives -= 1
        updateLivesImages()
        Toast.makeText(this, "Crash", Toast.LENGTH_SHORT).show()
        vibrateOnce()

        if (lives <= 0) {
            gameRunning = false
            handler.postDelayed({
                startGame()
            }, 1500)
        }
    }

    private fun vibrateOnce() {
        try {
            vibrator.vibrate(150)
        } catch (_: Exception) {
        }
    }

    private fun updateLivesImages() {
        life1Image.alpha = if (lives >= 1) 1f else 0.2f
        life2Image.alpha = if (lives >= 2) 1f else 0.2f
        life3Image.alpha = if (lives >= 3) 1f else 0.2f
    }

    private fun clearAllObstacles() {
        obstacleList.forEach { gameArea.removeView(it) }
        obstacleList.clear()
    }
}
