package com.example.mybird


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.mybird.sprites.Bird
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class UserAccount(
    val email: String,
    val accountName: String,
    var score: Int = 0,
    var listBirdPurchased: String = "bird1_down",
    var coinGold: Int = 0,
    var birdUsed:String = "bird1_down"
)

class FirebaseManager {

    private lateinit var auth: FirebaseAuth
    val database = Firebase.firestore

    val nameDb = "users"
    val nameField = "nameAccount"
    val scoreField = "score"
    val coinGoldField = "coinGold"
    val listBirdPurchased = "listBirdPurchased"
    val birdUsedField = "birdUsed"

    fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

//    suspend fun createAccount(email: String, password: String): String {
//        return try {
//            auth.createUserWithEmailAndPassword(email, password).await()
//            "account created successfully"
//        } catch (e: Exception) {
//            if (e.message?.contains("already in use") == true) {
//                "Tài khoản đã tồn tại"
//            } else {
//                "Lỗi: ${e.message}"
//            }
//        }
//    }
//
//    fun createAccountName(name: String): String {
//        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
//
//        val userAccount = UserAccount(email = user.email ?: "", accountName = name)
//
//        val data = hashMapOf(
//            nameField to userAccount.accountName,
//            markField to userAccount.mark
//        )
//
//        database.collection(nameDb).document(userAccount.email)
//            .set(data)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//
//        return "name account created successfully"
//    }

    fun createAccount(email: String, password: String, callback: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback("account created successfully")
                } else {
                    // Xử lý lỗi nếu có
                    val exception = task.exception
                    val message = exception?.message ?: "Unknown error"
                    if (message.contains("already in use", ignoreCase = true)) {
                        callback("Account already exists")
                    } else {
                        callback("Error: $message")
                    }
                }
            }
            .addOnFailureListener { e ->
                callback("Created Account Fail: ${e.message}")
            }
    }

    fun createAccountName(name: String, callback: (String) -> Unit) {
        val user = auth.currentUser
        val userAccount = UserAccount(email = user?.email ?: "", accountName = name)
        val data = hashMapOf(
            nameField to userAccount.accountName,
            scoreField to userAccount.score,
            listBirdPurchased to userAccount.listBirdPurchased,
            coinGoldField to userAccount.coinGold,
            birdUsedField to userAccount.birdUsed
        )

        database.collection(nameDb).document(userAccount.email)
            .set(data)
            .addOnSuccessListener {
                callback("name account created successfully")
            }
            .addOnFailureListener { e ->
                callback("created fail: $e")
            }
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

    fun logoutAccount() {
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
                        var notif: String = ""
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

    fun getNameAccount(callback: (String) -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            val score = result.get(nameField)
            callback(score.toString())
        }
        return "GET nameAcc done!"
    }


    fun updateScore(mark: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val updateData = database.collection(nameDb).document(user.email.toString())

        updateData
            .update(scoreField, mark)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        return "Update Score done!"
    }

    fun getScore(callback: (String) -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            val score = result.get(scoreField)
            callback(score.toString())
        }
        return "GET Score done!"
    }

    fun updateCoin(coinGold: Int): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val updateData = database.collection(nameDb).document(user.email.toString())

        updateData
            .update(coinGoldField, coinGold)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        return "Update Score done!"
    }

    fun getCoinGold(callback: (String) -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            val coinGold = result.get(coinGoldField)
            callback(coinGold.toString())
        }
        return "GET Score done!"
    }


    fun updateBirds(newBird: String, onComplete: () -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            if (result != null) {
                val currentListBird = result.get(listBirdPurchased)
                val updatedList = if (currentListBird != "") {
                    "$currentListBird,$newBird"
                } else {
                    newBird
                }

                docRef.update(listBirdPurchased, updatedList)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }
            onComplete()
        }

        return "Update Birds done!"
    }

    fun getListBird(callback: (String) -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            val listBird = result.get(listBirdPurchased)
            callback(listBird.toString())
        }
        return "GET Score done!"
    }


    fun updateBirdUsed(nameBird: String): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val updateData = database.collection(nameDb).document(user.email.toString())

        updateData
            .update(birdUsedField, nameBird)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        return "Update Score done!"
    }

    fun getBirdUsed(callback: (String) -> Unit): String {
        val user = auth.currentUser ?: return "Bạn chưa đăng nhập"
        val docRef = database.collection(nameDb).document(user.email.toString())
        docRef.get().addOnSuccessListener { result ->
            val birdUsedName = result.get(birdUsedField)
            callback(birdUsedName.toString())
        }
        return "GET Score done!"
    }




    fun rankQuery(callback: (ArrayList<User>) -> Unit) {
        val userList: ArrayList<User> = arrayListOf()

        database.collection(nameDb).orderBy(scoreField, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEachIndexed { index, document ->
                    val name = document.get(nameField)
                    val mark = document.get(scoreField)
                    val email = document.id
                    val maskEmail = maskEmail(email)
                    val user =
                        User((index + 1).toString(), name.toString(), mark.toString(), maskEmail)
                    userList.add(user)
                }
                callback(userList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
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