package com.acmvit.c2c2021.repository

import com.acmvit.c2c2021.util.Resource
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter

class FirebaseRTD(
    private val databaseReference: DatabaseReference
) {
    fun <T : Any> getStreamAt(
        pathString: String,
        emit: (snap: DataSnapshot) -> T?
    ): Observable<Resource<T>> = Observable.create<Resource<T>> {
        it.onNext(Resource.loading(null))
        val eventListener = ValueEventListenerSimple(it, emit)
        val ref = databaseReference.child(pathString)
        ref.addValueEventListener(eventListener)
        it.setCancellable { ref.removeEventListener(eventListener) }
    }

    fun <T : Any> getValueAt(
        pathString: String,
        emit: (snap: DataSnapshot) -> T?
    ) = Observable.create<Resource<T>> { emitter ->
        emitter.onNext(Resource.loading(null))
        databaseReference.child(pathString).get().addOnSuccessListener {
            emitter.onNext(Resource.success(emit(it)))
        }.addOnFailureListener {
            emitter.onNext(Resource.error(it.toString(), null))
        }
    }

    fun <T : Any> getCachedValueAt( pathString: String, emit: (snap: DataSnapshot) -> T?): Observable<Resource<T>> =
        Observable.create<Resource<T>> {
            it.onNext(Resource.loading(null))
            val ref = databaseReference.child(pathString)
            ref.addListenerForSingleValueEvent(ValueEventListenerSimple(it, emit))
        }

    class ValueEventListenerSimple<T : Any> (
        private val emitter: ObservableEmitter<Resource<T>>,
        private val emit: (snap: DataSnapshot) -> T?
    ) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            emitter.onNext(Resource.success(emit(snapshot)))
        }

        override fun onCancelled(error: DatabaseError) {
            emitter.onNext(Resource.error(error.toString(), null))
        }
    }

    companion object {
        const val TIMELINE = "timeline"
        const val TIMINGS = "timings"
        const val TRACKS = "tracks"
        const val CURRENT_TIME_DIFF = ".info/serverTimeOffset"
    }
}