
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawfect.databinding.AnimalItemBinding
import com.example.pawfect.model.Animal

// https://www.youtube.com/watch?v=eUKVJf0aMG0n   (Adapter)

class AnimalAdapter (private val animalList:ArrayList<Animal>, private val listener:(Animal, Int) -> Unit) : RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {
    inner class ViewHolder(var animalItemBinding: AnimalItemBinding) : RecyclerView.ViewHolder(animalItemBinding.root){
        fun bindItem(animal: Animal){
            animalItemBinding.animalImage.setImageResource(animal.image)
            animalItemBinding.animalDescription.text = animal.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = AnimalItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(animalList[position])
        holder.itemView.setOnClickListener{
            listener(animalList[position], position)
        }
    }
}
