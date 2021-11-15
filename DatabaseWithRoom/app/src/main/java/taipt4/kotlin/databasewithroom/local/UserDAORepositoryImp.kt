package taipt4.kotlin.databasewithroom.local

import io.reactivex.rxjava3.core.Flowable
import taipt4.kotlin.databasewithroom.database.IUserRepository
import taipt4.kotlin.databasewithroom.model.User

class UserDAORepositoryImp(private val userDAO: UserDAO): IUserRepository {
    override val allUsers: Flowable<List<User>>
        get() = userDAO.allUsers

    override fun getUserById(userId: Int): Flowable<List<User>> {
        return userDAO.getUserById(userId)
    }

    override fun insertUser(vararg users: User) {
        userDAO.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        userDAO.updateUser(*users)
    }

    override fun deleteUser(user: User) {
        userDAO.deleteUser(user)
    }

    override fun deleteAllUsers() {
        userDAO.deleteAllUsers()
    }
}