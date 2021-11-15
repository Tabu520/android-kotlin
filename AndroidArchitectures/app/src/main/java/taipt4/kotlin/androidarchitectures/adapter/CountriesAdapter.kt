package taipt4.kotlin.androidarchitectures.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import taipt4.kotlin.androidarchitectures.R
import taipt4.kotlin.androidarchitectures.model.Country

class CountriesAdapter(private val countries: ArrayList<Country>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
    var listener: OnItemClickListener? = null

    fun updateCountries(newCountries: List<Country>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
    )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position], listener)
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val countryName = view.findViewById<TextView>(R.id.tvCountry)
        private val countryCapital = view.findViewById<TextView>(R.id.tvCapital)

        fun bind(country: Country, listener: OnItemClickListener?) {
            countryName.text = country.name
            countryCapital.text = country.capital
            itemView.setOnClickListener { listener?.onItemClick(country) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(country: Country)
    }

}