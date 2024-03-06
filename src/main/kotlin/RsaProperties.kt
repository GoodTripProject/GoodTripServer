import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "rsa")
data class RsaProperties(val privateKey: RSAPrivateKey, val publicKey: RSAPublicKey)