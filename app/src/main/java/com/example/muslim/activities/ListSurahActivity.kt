package com.example.muslim.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.muslim.R
import com.example.muslim.adapter.SurahAdapter
import com.example.muslim.adapter.SurahAdapter.onSelectData.rvSurah
import com.example.muslim.model.ModelSurah
import com.example.muslim.networking.Api
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListSurahActivity : AppCompatActivity(), SurahAdapter.onSelectData {

        var surahAdapter: SurahAdapter? = null
        var progressDialog: ProgressDialog? = null
        var modelSurah: MutableList<ModelSurah> = ArrayList()

        @SuppressLint("SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_list_surah)

            progressDialog = ProgressDialog(this)
            progressDialog!!.setTitle("Mohon Tunggu")
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage("Sedang menampilkan data...")

            listSurah()
        }

        private fun listSurah() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.URL_LIST_SURAH)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        for (i : Int in 0 until response.length()) {
                            try {
                                progressDialog!!.dismiss()
                                val dataApi = ModelSurah()
                                val jsonObject: JSONObject = response.getJSONObject(i)
                                dataApi.nomor = jsonObject.getString("nomor")
                                dataApi.nama = jsonObject.getString("nama")
                                dataApi.type = jsonObject.getString("type")
                                dataApi.ayat = jsonObject.getString("ayat")
                                dataApi.asma = jsonObject.getString("asma")
                                dataApi.arti = jsonObject.getString("arti")
                                dataApi.keterangan = jsonObject.getString("keterangan")
                                modelSurah.add(dataApi)
                                showListSurah()
                            }catch (e : JSONException) {
                                e.printStackTrace()
                                Toast.makeText(this@ListSurahActivity, "Gagal menampilkan dan data!",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@ListSurahActivity, "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun showListSurah() {
            surahAdapter = SurahAdapter(this@ListSurahActivity, modelSurah, this)
            rvSurah!!.adapter = surahAdapter
        }

        override fun onSelected(modelSurah: ModelSurah) {
            val intent = Intent(this@ListSurahActivity, DetailSurahActivity::class.java)
            intent.putExtra("detailSurah", "modelSurah")
            startActivity(intent) }

    }

    private fun Intent.putExtra(name: Any) {

    }

    private fun Any.SetLayoutManager(linearLayoutManager: Any) {

    }

    private fun ProgressDialog.setCancelMessage(s: String) {

    }

