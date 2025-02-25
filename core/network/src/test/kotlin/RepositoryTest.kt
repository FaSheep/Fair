import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.runBlocking
import org.fasheep.fair.core.network.GraphRepository
import org.fasheep.fair.core.network.service2.ByIdQuery
import org.junit.Test

class RepositoryTest {
    @Test
    fun testRe() {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://api.studio.thegraph.com/query/99199/assign/version/latest")
            .build()
        runBlocking {
            val result =
                apolloClient.query(ByIdQuery(id = Optional.present("0x899a43102cf7d3c1bac9fd5954efc3f49efd5dea9fbea59f25ba31b4e270037c")))
                    .execute().data?.rolesAssigned
                    ?: return@runBlocking
//            val assignments = result.names.zip(result.roles) { a, b -> Assignment(a, b) }
//            println(assignments)
        }
    }

    @Test
    fun te() {
        val graphRepository = GraphRepository(
            ApolloClient.Builder()
                .serverUrl("https://api.studio.thegraph.com/query/99199/random/version/latest")
                .build(),
            ApolloClient.Builder()
                .serverUrl("https://api.studio.thegraph.com/query/99199/assign/version/latest")
                .build(),
            ApolloClient.Builder()
                .serverUrl("https://api.studio.thegraph.com/query/99199/vote/version/latest")
                .build()
        )

        runBlocking {
            println(graphRepository.findVoteCreateById("0x631996f85d7cb1a3713338b9336281278738ed8361cf8f6c047caccca05e9798"))
        }
    }
}