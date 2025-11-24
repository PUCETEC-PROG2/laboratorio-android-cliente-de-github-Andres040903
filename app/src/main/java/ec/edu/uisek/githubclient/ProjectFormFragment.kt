package ec.edu.uisek.githubclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProjectFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_project_form, container, false)

        val btnCancel = view.findViewById<View>(R.id.btnCancel)
        val btnSave = view.findViewById<View>(R.id.btnSaveProject)

        // Botón CANCELAR → regresar a la lista
        btnCancel.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .popBackStack()
        }

        // Botón GUARDAR → mensaje + regresar a la lista
        btnSave.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Proyecto guardado (local, no en GitHub)",
                Toast.LENGTH_SHORT
            ).show()

            requireActivity()
                .supportFragmentManager
                .popBackStack()
        }

        return view
    }
}
