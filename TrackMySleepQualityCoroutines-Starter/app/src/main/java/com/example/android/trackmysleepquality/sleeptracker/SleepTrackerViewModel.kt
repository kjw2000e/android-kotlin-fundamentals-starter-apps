/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {
                // context 사용하려면 AndroidViewModel을 사용해야한다.

        private var tonight = MutableLiveData<SleepNight?>()

        private val nights = database.getAllNights()

        val nightsString = Transformations.map(nights) { nights ->
                formatNights(nights, application.resources)
        }

        // 코루틴을 메인 스레드에서 실행한다. 결과값이 UI에 영향을 미치기 때문
        // 오래 걸리는 작업을 하는 suspend function을 호출한다, 결과를 기다리는 동안 UI thread를 block 하지 않도록
        // 오래 걸리는 작업은 UI와 관련 없으므로 I/O context로 스위칭한다. 이러한 종류를 위해 최적화를 하고 별도로 설정된 스레드 풀에서 작업을 실행할 수 있다.
        // 그런 다음 오래걸리는 작업을 호출한다
        // Use a Transformations map to create a string from a LiveData object every time the object changes.
        // 라이브데이터 객체가 객체 변환이 있을 때 string으로 생성하여 반환 시켜주는 transformation map 을 사용하라.
        init {
            initializeTonight()
        }

        private fun initializeTonight() {
                viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

        private suspend fun getTonightFromDatabase(): SleepNight? {
                var night = database.getTonight()
                if (night?.endTimeMilli != night?.startTimeMilli) {
                        night = null
                }
                return night
        }

        // start btn onclick event
        // new instance insert to table
        fun onStartTracking() {
                viewModelScope.launch {
                        val newNight = SleepNight()
                        insert(newNight)
                        tonight.value = getTonightFromDatabase()
                }
        }

        fun onStopTracking() {
                viewModelScope.launch {
                        // return@launch  return@label 구문은 여러 중첩 함수 중에서 이 문이 반환하는 함수를 지정
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilli = System.currentTimeMillis()
                        update(oldNight)
                }
        }

        fun onClear() {
                viewModelScope.launch {
                        clear()
                        tonight.value = null
                }
        }

        private suspend fun insert(night: SleepNight) {
                // Room은 Dispatchers.IO 사용하여 main thread에서는 아무것도 영향을 끼치지 않음
//                withContext(Dispatchers.IO) {database.insert(night)} // room 이 아닌 곳에서는 Dispatchers를 써줘야한다.
                database.insert(night)
        }

        private suspend fun update(night: SleepNight) {
                database.update(night)
        }

        private suspend fun clear() {
                database.clear()
        }
}

