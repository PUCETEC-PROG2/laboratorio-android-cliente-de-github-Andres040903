package ec.edu.uisek.githubclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ec.edu.uisek.githubclient.databinding.FragmentRepoItemBinding
import ec.edu.uisek.githubclient.models.Repo

class RepoViewHolder(
    val binding: FragmentRepoItemBinding
) : RecyclerView.ViewHolder(binding.root)

class ReposAdapter(
    private val onEdit: (Repo) -> Unit,
    private val onDelete: (Repo) -> Unit
) : RecyclerView.Adapter<RepoViewHolder>() {

    private var repositories: List<Repo> = emptyList()

    override fun getItemCount(): Int = repositories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = FragmentRepoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repositories[position]
        val binding = holder.binding

        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description ?: "El repositorio no tiene descripcion"
        binding.repoLang.text = repo.language ?: "Lenguaje no especificado"

        Glide.with(binding.root.context)
            .load(repo.owner.avatarURL)
            .placeholder(R.mipmap.ic_launcher)
            .circleCrop()
            .into(binding.repoOwnerImage)

        // ✔ IDs correctos: buttonEdit y buttonDelete
        binding.buttonEdit.setOnClickListener {
            onEdit(repo)
        }

        binding.buttonDelete.setOnClickListener {
            onDelete(repo)
        }
    }

    fun updateRepositories(newRepos: List<Repo>) {
        repositories = newRepos
        notifyDataSetChanged()
    }
}
