package com.example.mybird


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
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
        val userAccount = UserAccount(email = user.email ?: "", accountName = name)

        database.child("users").child(user.uid).setValue(userAccount).await()
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

    suspend fun updateMark(nameAccount: String, mark: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val userRef = database.child("users").child(user.uid)

        return try {
            userRef.child("mark").setValue(mark).await()
            "Cập nhật điểm thành công"
        } catch (e: Exception) {
            "Lỗi: ${e.message}"
        }
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