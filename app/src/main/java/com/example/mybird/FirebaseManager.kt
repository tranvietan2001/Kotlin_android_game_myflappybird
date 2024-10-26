package com.example.mybird


import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class UserAccount(val email: String, val accountName: String, var mark: Int = 0)

class FirebaseManager {

    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference

    val database = Firebase.firestore
    val nameDb = "users"
    val markField = "mark"
    val nameField = "nameAccount"

    fun initFirebase() {
        auth = FirebaseAuth.getInstance()
//        database = Firebase.database.reference
    }

    suspend fun createAccount(email: String, password: String): String {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            "Tạo tài khoản thành công"
        } catch (e: Exception) {
            if (e.message?.contains("already in use") == true) {
                "Tài khoản đã tồn tại"
            } else {
                "Lỗi: ${e.message}"
            }
        }
    }

    fun createAccountName(name: String): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"

        val userAccount = UserAccount(email = user.email ?: "", accountName = name)

        val db = Firebase.firestore
        val data = hashMapOf(
            nameField to userAccount.accountName,
            markField to userAccount.mark
        )

        db.collection(nameDb).document(userAccount.email)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        return "Tạo tên người chơi thành công"
    }

    fun loginAccount(email: String, password: String, callback: (String) -> Unit) {
        logoutAccount()
//        return try {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.collection(nameDb).document(email).get().addOnSuccessListener { result ->
                        val name = result.get(nameField)
                        callback(name.toString())
                    }

//                    callback("ok") // Trả về "ok" nếu đăng nhập thành công
                } else {
                    callback("fail") // Trả về "ng" nếu đăng nhập không thành công
                }
            }
    }

    private fun logoutAccount() {
        auth.signOut()
    }

    fun updateMark(nameAccount: String, mark: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val updateData = database.collection(nameDb).document(user.email.toString())

// Set the "isCapital" field of the city 'DC'
        updateData
            .update(markField, mark)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        // truy van sap xep

        val testList: MutableList<String> = mutableListOf()

        database.collection(nameDb).orderBy(markField, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    testList.add(document.id+"-"+document.data)
                    println("=====>${document.id} === ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return "Update done!"
    }

    suspend fun getMark(nameAccount: String): Any {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
//        val mark = database.child("users").child(user.uid).child("mark").get().await().getValue(Int::class.java)
        return "Không tìm thấy thông tin"
    }

}