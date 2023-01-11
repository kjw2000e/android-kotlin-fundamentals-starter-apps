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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(
    val key: Long,
    val dao: SleepDatabaseDao
) : ViewModel() {
    // sleep quality fragment 로 돌아가기 위한 변수
    private val _isUpdated = MutableLiveData<Boolean?>()
    val isUpdated: LiveData<Boolean?>
        get() = _isUpdated

    init {

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onSetSleepQuality(quality: Int) { // click handler
        viewModelScope.launch {
            val tonight = dao.get(key) ?: return@launch
            tonight.sleepQuality = quality
            dao.update(tonight)
            _isUpdated.value = true
        }
    }

    fun onDoneNavigate() {
        _isUpdated.value = null
    }
}
