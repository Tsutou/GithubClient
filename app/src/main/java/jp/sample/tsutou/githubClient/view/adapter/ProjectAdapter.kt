package jp.sample.tsutou.githubClient.view.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.databinding.ProjectListItemBinding
import jp.sample.tsutou.githubClient.service.model.Project
import jp.sample.tsutou.githubClient.view.callback.ProjectClickCallback


class ProjectAdapter(private val projectClickCallback: ProjectClickCallback?) :
        RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    private var projectList: List<Project>? = null

    fun setProjectList(projectList: List<Project>) {

        if (this.projectList == null) {
            this.projectList = projectList

            notifyItemRangeInserted(0, projectList.size)

        } else {

            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@ProjectAdapter.projectList).size
                }

                override fun getNewListSize(): Int {
                    return projectList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return requireNotNull(this@ProjectAdapter.projectList)[oldItemPosition].id == projectList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = projectList[newItemPosition]
                    val old = projectList[oldItemPosition]

                    return project.id == old.id && project.git_url == old.git_url
                }
            })
            this.projectList = projectList

            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ProjectViewHolder {
        val binding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.project_list_item, parent,
                        false) as ProjectListItemBinding

        binding.callback = projectClickCallback

        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.binding.project = requireNotNull(projectList)[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (projectList == null) 0 else requireNotNull(projectList).size
    }

    open class ProjectViewHolder(val binding: ProjectListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
