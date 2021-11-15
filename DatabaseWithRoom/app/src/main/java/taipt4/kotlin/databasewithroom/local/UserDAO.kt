package taipt4.kotlin.databasewithroom.local

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import taipt4.kotlin.databasewithroom.model.User

@Dao
interface UserDAO {

    @get:Query("SELECT * FROM users")
    val allUsers: Flowable<List<User>>

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId: Int): Flowable<List<User>>

    @Insert
    fun insertUser(vararg users: User)

    @Update
    fun updateUser(vararg users: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM users")
    fun deleteAllUsers()
}