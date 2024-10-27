package com.example.mybird


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class UserAccount(val email: String, val accountName: String, var mark: Int = 0)

class FirebaseManager {

    private lateinit var auth: FirebaseAuth
    val database = Firebase.firestore

    val nameDb = "users"
    val markField = "mark"
    val nameField = "nameAccount"

    fun initFirebase() {
        auth = FirebaseAuth.getInstance()
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

        val data = hashMapOf(
            nameField to userAccount.accountName,
            markField to userAccount.mark
        )

        database.collection(nameDb).document(userAccount.email)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        return "Tạo tên người chơi thành công"
    }

    fun loginAccount(email: String, password: String, callback: (String) -> Unit) {
        logoutAccount()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.collection(nameDb).document(email).get()
                        .addOnSuccessListener { result ->
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

    fun forgotPass(email: String, callback: (String) -> Unit) {
        database.collection(nameDb)
            .get()
            .addOnSuccessListener { result ->
                var isCheck: Boolean = false
                for (document in result) {
                    if (document.id == email) {
                        isCheck = true
                        break
                    }
//                    println("ISCHECK: ${document.id} => $email == ${document.data}")
                }

                if (isCheck) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        var notif:String = ""
                        if (task.isSuccessful) {
                            callback("checkMail")
                        } else callback("resetFail")
                    }
                } else callback("emailNotExist")
//                println("ISCHECK: $isCheck")
            }
            .addOnFailureListener { exception ->
                callback("There is a problem with the connection, please check again.")
                println("ISCHECK: Error getting documents.$exception")
            }
    }

    fun updateMark(nameAccount: String, mark: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val updateData = database.collection(nameDb).document(user.email.toString())

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

    fun rankQuery(callback: (ArrayList<User>) -> Unit) {
        val userList: ArrayList<User> = arrayListOf()

        database.collection(nameDb).orderBy(markField, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
//                for (document in result) {
////                    testList.add(document.id+"-"+document.data)
//                    println("=====>${document.id} === ${document.data}")
//                    val email = document.id
//                    val name = document.get(nameField)
//                    val mark = document.get(markField)
//                    val user = User(name.toString(), mark.toString(), email)
//                    userList.add(user)
//                }

                result.documents.forEachIndexed { index, document ->
                    val name = document.get(nameField)
                    val mark = document.get(markField)
                    val email = document.id
                    val maskEmail = maskEmail(email)
                    val user = User((index+1).toString(), name.toString(), mark.toString(), maskEmail)
                    userList.add(user)
                }



                callback(userList)
//                println("ARR -t: " + userList.size.toString())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
//        println("ARR: -s" + userList.size.toString())
    }

    suspend fun getMark(nameAccount: String): Any {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
//        val mark = database.child("users").child(user.uid).child("mark").get().await().getValue(Int::class.java)
        return "Không tìm thấy thông tin"
    }




    fun maskEmail(email: String): String {
        // Tách phần tên và phần miền
        val atIndex = email.indexOf('@')
        if (atIndex <= 1 || atIndex == email.length - 1) {
            return email // Trả về email gốc nếu không hợp lệ
        }

        val namePart = email.substring(0, atIndex) // Phần trước dấu '@'
        val domainPart = email.substring(atIndex) // Phần sau dấu '@'

        // Lấy chữ đầu và chữ cuối
        val firstChar = namePart[0]
        val lastChar = namePart[namePart.length - 1]

        // Tạo chuỗi với ký tự '*' cho các ký tự ở giữa
        val maskedNamePart = "$firstChar${"*".repeat(namePart.length - 2)}$lastChar"

        // Kết hợp lại
        return maskedNamePart + domainPart
    }


}