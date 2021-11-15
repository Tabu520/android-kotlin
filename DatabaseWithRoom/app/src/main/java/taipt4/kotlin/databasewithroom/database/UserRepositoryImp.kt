package taipt4.kotlin.databasewithroom.database

import io.reactivex.rxjava3.core.Flowable
import taipt4.kotlin.databasewithroom.model.User

class UserRepositoryImp(private val mUserRepository: IUserRepository): IUserRepository {

    override val allUsers: Flowable<List<User>>
        get() = mUserRepository.allUsers

    override fun getUserById(userId: Int): Flowable<List<User>> {
        return mUserRepository.getUserById(userId)
    }

    override fun insertUser(vararg users: User) {
        mUserRepository.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        mUserRepository.updateUser(*users)
    }

    override fun deleteUser(user: User) {
        mUserRepository.deleteUser(user)
    }

    override fun deleteAllUsers() {
        mUserRepository.deleteAllUsers()
    }
}