package com.example.mybird


import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class UserAccount(val email: String, val accountName: String, var mark: Int = 0)

class FirebaseManager {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
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

    suspend fun createAccountName(name: String): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
//        return user.email.toString()

        val userAccount = UserAccount(email = user.email ?: "", accountName = name)

//        database.child("users").child(user.uid).setValue(userAccount).await()
//        https://firebase.google.com/docs/firestore/manage-data/add-data?hl=vi#kotlin+ktx


        val db = Firebase.firestore
        val data = hashMapOf(
//            "email" to userAccount.email,
            "nameAccount" to userAccount.accountName,
            "Mark" to userAccount.mark
        )

        //dcm id random
//        db.collection("users")
//            .add(data)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }

        db.collection("users").document(userAccount.email)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        return "Tạo tên người chơi thành công"
    }

    suspend fun loginAccount(email: String, password: String): String {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: return "Tài khoản chưa được đăng ký"
            val name = database.child("users").child(user.uid).child("accountName").get().await().getValue(String::class.java)
            name ?: "Tên tài khoản không có"
        } catch (e: Exception) {
            if (e.message?.contains("wrong password") == true) {
                "Mật khẩu sai, vui lòng kiểm tra và đăng nhập lại"
            } else {
                "Tài khoản chưa được đăng ký"
            }
        }
    }

    fun logoutAccount(){
        auth.signOut()
    }

    fun updateMark(nameAccount: String, mark: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"

        val db = Firebase.firestore
        val updateData = db.collection("users").document(user.email.toString())

// Set the "isCapital" field of the city 'DC'
        updateData
            .update("Mark", mark)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


        val dataList = db.collection("users")
        val rs = dataList.orderBy("Mark").limit(3)
        return rs.toString()
    }

    suspend fun getMark(nameAccount: String): Any {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val mark = database.child("users").child(user.uid).child("mark").get().await().getValue(Int::class.java)
        return mark ?: "Không tìm thấy thông tin"
    }

    suspend fun arrangeMark(): List<Triple<String, Int, Int>> {
        val snapshot = database.child("users").get().await()
        val userList = mutableListOf<UserAccount>()

        snapshot.children.forEach { child ->
            val userAccount = child.getValue(UserAccount::class.java)
            if (userAccount != null) {
                userList.add(userAccount)
            }
        }

        val sortedList = userList.sortedByDescending { it.mark }.take(20)
        val result = sortedList.mapIndexed { index, user ->
            Triple(user.accountName, user.mark, index + 1)
        }

        return result
    }
}