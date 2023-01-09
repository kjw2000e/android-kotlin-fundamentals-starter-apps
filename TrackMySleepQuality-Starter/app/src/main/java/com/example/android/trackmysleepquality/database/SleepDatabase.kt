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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 이 코드는 어떤 room dabtabase 의 내용과 거의 같기 때문에 템플릿 처럼 사용하면 된다.
// entity, version을 설정한다. entity는 여러개 설정할 수 있다.
// exportSchema = false : 스키마 버전 기록 백업을 유지 하지 않음, 스키마 버전 정보를 파일로 export함, default true
// false로 설정하는 것이 일반적? 이다.


// DAO 필요 : 데이터베이스에 접근하기 위해, abstract로 val 로 정의
// database 에 접근하기 위한 객체를 companion object로 정의
// nullable instance 로 체크
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        // Volatile : 휘발성 변수 , 캐시되지 않으며 모든 쓰기 및 읽기는 주 메모리에서 수행됨.
        // 이렇게 하면 instance의 값이 항상 최신 상태이고 모든 실행 스레드에서 동일함
        // 한 스레드가 instance에 변경한 사항을 다른 모든 스레드가 즉시 볼 수 있으며 두 스레드가 각각 캐시에서 동일한 엔터티를 업데이트 하면서 문제를 일으키는 상황이 발생하지 않는다.
        // Volatile 어노테이션은 변경 가능한 변수 var로 선언해야함.
        // synchronized : 데이터베이스 객체가 한번만 생성되기 위한 장치 (this)는 context에 접근하기 위해 사용
        // (멀티 스레드 환경) 서로 다른 스레드에서 동시에 데이테베이스 인스턴스를 요청할 수 있으므로 2개의 인스턴스가 생성될 수 있기 때문에 synchronized를 사용해 하나의 스레드에서 진입 시 다른 스레드에서 접근하지 못하도록 block 시킨다.
        // 한번에 하나의 실행 스레드만 블록안으로 들어갈 수 있다.
        // fallbackToDestructiveMigration method : 마이그레이션 전략?
        // 일반적으로 스키마 변경 사항에 대하 마이그레이션 전략과 함께 마이그레이션 객체를 제공해야한다.
        // 마이그레이션 객체는 데이터 손실이 없도록 이전 스키마의 모든 행을 가져와 새 스키마의 행으로 변환하는 방법을 정의하는 객체이다.
        // 가장 간단한 방법은 이전 데이터를 다 삭제하고 재구축 하는 것이다.
        // fallbackToDestructiveMigration 은 이전 스키마에 대한 모든 데이터를 삭제하고 새로운 스키마에 대한 db를 생성하는 전략
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE // // kotlin smart casting 활용
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database" //db name
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
