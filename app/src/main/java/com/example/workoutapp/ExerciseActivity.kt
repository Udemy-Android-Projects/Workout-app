package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.ActivityExerciseBinding
import com.example.workoutapp.databinding.CustomDialogBackButtonBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null
    private var restTimer : CountDownTimer? = null
    private var restProgress : Int = 0
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress : Int = 0
    private var restTime : Int = 5
    private var exerciseTime : Int = 10
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercise : Int = -1
    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        tts = TextToSpeech(this,this)

        exerciseList = Constants.defaultExerciseList()

        //Setting up the toolbar
        setSupportActionBar(binding?.tbExercise)
        //Adding a back button
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbExercise?.setNavigationOnClickListener{
            customBackButton()
        }
        setUpRestView(restTime)
        setUpExerciseStatusRecyclerView()

    }

    //override onBackPressed
    override fun onBackPressed() {
        customBackButton()
    }

    private fun customBackButton() {
        val customDialog = Dialog(this)
        //Inflate the custom dialog layout file
        val dialogBinding = CustomDialogBackButtonBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        //can not cancel dialog by clicking outside of it
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener{
            //The activity to be closed must be specified
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setUpRestView(totalRestTimeI : Int){
        if(restTimer != null){
            restTimer?.cancel()
            restProgress= 0
        }
        try{
            val soundURI = Uri.parse("android.resource://com.example.workoutapp/" + R.raw.ding)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch(e:Exception){
            e.printStackTrace() }

            //Rest timer widgets made visible
            binding?.flProgressBar?.visibility = View.VISIBLE
            binding?.tvTitle?.visibility= View.VISIBLE
            binding?.restViewText?.visibility = View.VISIBLE
            binding?.restViewExerciseName?.visibility = View.VISIBLE
            //Exercise timer widgets visibility = gone. This is to allow only one timer to exist at a time
            binding?.flProgressBarExercise?.visibility = View.GONE
            binding?.ivImage?.visibility= View.GONE
            binding?.tvExerciseName?.visibility = View.GONE
            binding?.restViewExerciseName?.text = exerciseList!![currentExercise + 1].getName()
            speakOut("We will now rest for $restTime seconds")


        setRestProgressBar(totalRestTimeI)
    }

    private fun setRestProgressBar(totalRestTime : Int){
        restTimer = object : CountDownTimer((totalRestTime * 1000).toLong(),1000){
            //What should happen every tick
            override fun onTick(p0: Long) {
                binding?.progressBar?.max = totalRestTime
                restProgress++
                binding?.progressBar?.progress = totalRestTime - restProgress
                binding?.tvTimer?.text = "${totalRestTime - restProgress}"
            }
            //What should happen at the end
            override fun onFinish() {
                currentExercise++
                //When the rest timer is over the next exercise is automatically selected
                exerciseList!![currentExercise].setIsSelected(true)
                //To change recyclerView appearance
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView(exerciseTime)
            }
        }.start() //Don't forget to start
    }

    private fun setUpExerciseView(totalExerciseTimeI : Int){
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }
        //Rest view widgets visibility = invisible. This is because other widgets position are relative to rest view widgets and therefore their existence cant be completely erased
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility=View.INVISIBLE
        binding?.restViewText?.visibility = View.INVISIBLE
        binding?.restViewExerciseName?.visibility = View.INVISIBLE

        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flProgressBarExercise?.visibility = View.VISIBLE
        binding?.ivImage?.visibility=View.VISIBLE
        //Position starts from zero but the ID for this exercise is 1
        binding?.ivImage?.setImageResource(exerciseList!![currentExercise].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercise].getName()

        //Manifest queries added
        speakOut("Start ${exerciseList!![currentExercise].getName()}")
        setExerciseProgressBar(totalExerciseTimeI)
    }

    private fun setExerciseProgressBar(totalExerciseTime : Int){
        //CountDownTimer uses milliseconds
        exerciseTimer = object : CountDownTimer((totalExerciseTime * 1000).toLong(),1000){
            override fun onTick(p0: Long) {
                binding?.progressBarExercise?.max = totalExerciseTime
                exerciseProgress++
                binding?.progressBarExercise?.progress = totalExerciseTime - exerciseProgress
                binding?.tvTimerExercise?.text = "${totalExerciseTime - exerciseProgress}"
            }
            override fun onFinish() {
                exerciseList!![currentExercise].setIsSelected(false)
                exerciseList!![currentExercise].setIsCompleted(true)
                //To change recyclerView appearance
                exerciseAdapter!!.notifyDataSetChanged()
                if(currentExercise < exerciseList?.size!! - 1){
                    setUpRestView(restTime)
                }else{
                    //Close all the activities in the stack
                    finish()
                    startActivity(Intent(this@ExerciseActivity,FinishActivity::class.java))
                }
            }
        }.start()
    }

    private fun speakOut(text : String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        //This therefore means that the method will be called after the exerciseList has been instantiated
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(tts!= null){
            tts?.stop()
            tts?.shutdown()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Lang not supported")
            }else{
                Log.e("TTS","Init failure")
            }
        }
    }
}