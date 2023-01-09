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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

// DAO는 코틀린 함수를 데이터베이스 쿼리에 맵핑한다.
// dao 는 인터페이스
@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(sleepNight: SleepNight)

    @Insert
    fun insertAll(list: MutableList<SleepNight>)

    @Update
    fun update(sleepNight: SleepNight)

    @Query("Select * FROM daily_sleep_quality_table WHERE nitghtId = :key")
    fun get(key: Long): SleepNight?

    // @Delete 어노테이션은 해당 아이템만 삭제 , 전달인자(argument)의 primary key 에 대응하는 아이템 삭제
    // 대응되는 아이템이 없으면 아무것도 변경하지 않음.
    // vararg 를 써줘야함
    @Delete
    fun deleteItem(vararg sleepNight: SleepNight)

    // 전체 data 삭제
    @Query("Delete FROM daily_sleep_quality_table Where nitghtId = :key")
    fun clear(key: Long)


    // 용도 : 테이블이 비었는지 확인 isEmpty 비었으면 null 리턴 시키도록 nullable 리턴
    // "LIMIT 숫자 " 는 한개의 element만 반환한다.
    @Query("Select * from daily_sleep_quality_table ORDER BY nitghtId DESC LIMIT 1")
    fun getTonight(): SleepNight? // return nullable object

    @Query("SELECT * FROM DAILY_SLEEP_QUALITY_TABLE ORDER BY nitghtId DESC")
    fun getAllNight(): LiveData<List<SleepNight>>

}
