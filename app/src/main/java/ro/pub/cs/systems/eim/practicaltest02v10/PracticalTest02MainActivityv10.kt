package ro.pub.cs.systems.eim.practicaltest02v10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class PracticalTest02MainActivityv10 : AppCompatActivity() {
    private lateinit var pokemonNameEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonTypesTextView: TextView
    private lateinit var pokemonAbilitiesTextView: TextView
    private lateinit var nextActivityButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v10_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pokemonNameEditText = findViewById(R.id.pokemon_name)
        searchButton = findViewById(R.id.search_button)
        pokemonImageView = findViewById(R.id.pokemon_image)
        pokemonTypesTextView = findViewById(R.id.pokemon_types)
        pokemonAbilitiesTextView = findViewById(R.id.pokemon_abilities)
        nextActivityButton = findViewById(R.id.next_activity_button)

        searchButton.setOnClickListener {
            val pokemonName = pokemonNameEditText.text.toString().trim()
            if (pokemonName.isNotEmpty()) {
                fetchPokemonData(pokemonName)
            }
        }

        nextActivityButton.setOnClickListener {
            val intent = Intent(this, Seccondactivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPokemonData(pokemonName: String) {
        val url = "https://pokeapi.co/api/v2/pokemon/$pokemonName"
        Log.d("PokemonData", "Fetching data from URL: $url")
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.e("PokemonData", "Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val responseString = responseBody.string()
                    Log.d("PokemonData", "Response: $responseString")

                    val json = JSONObject(responseString)
                    val types = json.getJSONArray("types")
                    val abilities = json.getJSONArray("abilities")
                    val imageUrl = json.getJSONObject("sprites").getString("front_default")

                    val typesList = mutableListOf<String>()
                    for (i in 0 until types.length()) {
                        val type = types.getJSONObject(i).getJSONObject("type").getString("name")
                        typesList.add(type)
                    }

                    val abilitiesList = mutableListOf<String>()
                    for (i in 0 until abilities.length()) {
                        val ability =
                            abilities.getJSONObject(i).getJSONObject("ability").getString("name")
                        abilitiesList.add(ability)
                    }

                    val formattedTypes = "Types: ${typesList.joinToString(", ")}"
                    val formattedAbilities = "Abilities: ${abilitiesList.joinToString(", ")}"

                    Log.d("PokemonData", "Parsed Types: $formattedTypes")
                    Log.d("PokemonData", "Parsed Abilities: $formattedAbilities")

                    runOnUiThread {
                        pokemonTypesTextView.text = formattedTypes
                        pokemonAbilitiesTextView.text = formattedAbilities
                        Glide.with(this@PracticalTest02MainActivityv10).load(imageUrl)
                            .into(pokemonImageView)
                    }
                }
            }
        })
    }
}