package taipt4.kotlin.databasewithroom.database

import io.reactivex.rxjava3.core.Flowable
import taipt4.kotlin.databasewithroom.model.User

interface IUserRepository {

    val allUsers: Flowable<List<User>>

    fun getUserById(userId: Int): Flowable<List<User>>

    fun insertUser(vararg users: User)

    fun updateUser(vararg users: User)

    fun deleteUser(user: User)

    fun deleteAllUsers()
}