import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import java.util.Optional

@Service
@RequiredArgsConstructor
class UserService {
    @Autowired
    private lateinit var repository: AuthenticationRepository

    //TODO перевести

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    fun getByUsername(username: String): User? {
        try {
            val user = repository.getUserByEmail(username).get()
            return user
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    /**
     * Получение пользователя по имени пользователя
     *
     *
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String -> this.getByUsername(username) }
    }

    val currentUser: User?
        /**
         * Получение текущего пользователя
         *
         * @return текущий пользователь
         */
        get() {
            // Получение имени пользователя из контекста Spring Security
            val username = SecurityContextHolder.getContext().authentication.name
            return getByUsername(username)
        }
}