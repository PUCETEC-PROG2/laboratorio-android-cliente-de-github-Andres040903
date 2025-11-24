package ec.edu.uisek.githubclient

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchRepositories()

        // AQUI ACTIVAMOS EL BOTÓN +
        binding.fabAddRepo.setOnClickListener {
            binding.fragmentContainer.visibility = View.VISIBLE
            openProjectForm()
        }
    }

    // Abre el formulario cuando presiones el botón +
    private fun openProjectForm() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, ProjectFormFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setupRecyclerView() {

        reposAdapter = ReposAdapter(
            onEdit = { repo ->
                Toast.makeText(this, "Editar: ${repo.name}", Toast.LENGTH_SHORT).show()
            },
            onDelete = { repo ->
                Toast.makeText(this, "Eliminar: ${repo.name}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.repoRecyclerView.adapter = reposAdapter
    }

    private fun fetchRepositories() {
        val apiService = RetrofitClient.gitHubApiService
        val call = apiService.getRepos()

        call.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

                if (response.isSuccessful) {
                    val repos = response.body()

                    if (repos != null && repos.isNotEmpty()) {
                        reposAdapter.updateRepositories(repos)
                    } else {
                        showMessage("Usted no tiene repositorios")
                    }

                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Error de autenticación"
                        403 -> "Recurso no permitido"
                        404 -> "Recurso no encontrado"
                        else -> "Error desconocido ${response.code()}"
                    }

                    Log.e("MainActivity", errorMsg)
                    showMessage(errorMsg)
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                showMessage("Error de conexión")
                Log.e("MainActivity", "Error de conexión: ${t.message}")
            }
        })
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
