/*
 *  Copyright 2021 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.gameoflife.controller

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import edu.cnm.deepdive.gameoflife.R
import edu.cnm.deepdive.gameoflife.databinding.ActivityMainBinding
import edu.cnm.deepdive.gameoflife.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel? = null
    private var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        ViewModelProvider(this).get(MainViewModel::class.java).also {
            viewModel = it
            lifecycle.addObserver(it)
            it.running.observe(this, { running: Boolean ->
                if (running != this.running) {
                    this.running = running
                    invalidateOptionsMenu()
                }
            })
            binding.viewModel = it;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.actions, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        with (menu){
            findItem(R.id.run).isVisible = !running
            findItem(R.id.pause).isVisible = running
            findItem(R.id.reset).isVisible = !running
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var handled = true
        when (item.itemId) {
            R.id.run -> viewModel!!.start()
            R.id.pause -> viewModel!!.stop()
            R.id.reset -> viewModel!!.reset()
            else -> handled = super.onOptionsItemSelected(item)
        }
        return handled
    }
}