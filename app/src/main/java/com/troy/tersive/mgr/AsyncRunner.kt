package com.troy.tersive.mgr

import androidx.room.RoomDatabase
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import timber.log.Timber

/**
 * Lightweight runner of asynchronous tasks (lambdas). It also supports unit testing much better by providing an immediate runner.
 */
interface AsyncRunner {

    val cc: CoroutineContextProvider

    fun doJob(task: () -> Unit)
    fun doDatabase(task: () -> Unit)
    fun doNetwork(task: () -> Unit)
    fun doUI(task: suspend CoroutineScope.() -> Unit): Job

    fun doTransaction(db: RoomDatabase, task: () -> Unit) {
        if (db.inTransaction()) {
            task()
        } else {
            doDatabase {
                db.runInTransaction(task)
            }
        }
    }

    object MainRunner : AsyncRunner {

        override val cc = CoroutineContextProvider.MainCoroutineContextProvider

        override fun doJob(task: () -> Unit) {
            background { task() }
        }

        override fun doDatabase(task: () -> Unit) {
            background { task() }
        }

        override fun doNetwork(task: () -> Unit) {
            background { task() }
        }

        override fun doUI(task: suspend CoroutineScope.() -> Unit) = launch(cc.ui, block = task)

        private fun background(task: suspend CoroutineScope.() -> Unit): Job {
            return launch(cc.commonPool + CoroutineExceptionHandler { _, e ->
                Timber.e(e)
            }, block = task)
        }
    }

    object TestRunner : AsyncRunner {

        override val cc = CoroutineContextProvider.TestJDBCCoroutineContextProvider

        override fun doJob(task: () -> Unit) = task()
        override fun doDatabase(task: () -> Unit) = task()
        override fun doNetwork(task: () -> Unit) = task()
        override fun doUI(task: suspend CoroutineScope.() -> Unit) = launch(cc.ui, block = task)
    }
}
