package com.example.antenna.interest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.antenna.BuildConfig
import com.example.antenna.R
import com.example.antenna.adpater.CompanyAdapter
import com.example.antenna.adpater.CompanyList
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.android.synthetic.main.add_company.*
import java.io.FileReader
import java.io.IOException

class InterestActivity : AppCompatActivity(){

    private val list = ArrayList<CompanyList>()
    private val adapter1 = CompanyAdapter(list)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_company)

        // searchView 검색 및 변경 처리
        // search_searchView.isSubmitButtonEnabled = true

        search_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 누르면 호출 되는 함수
                Log.d("Change SEARCH TEXT : ", query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                Log.d("onQueryTextChange : ", newText.toString())

                // 검색창에서 글자가 변경이 일어날 떄마다 호출되는 함수
                adapter1.filter.filter(newText)
                adapter1.notifyDataSetChanged()

                return true
            }
        })

        // CSV READER
        val filePath = filesDir.toString()
        val csvHelper = CsvHelper(filePath)

        val stockList = "stockList.csv"
        val dataList = csvHelper.readAllCsvData(stockList)

        for(data in dataList){
            list.add(CompanyList(data[0], data[1]))
        }

        rv_data.adapter = adapter1
    }

    class CsvHelper(private val filePath: String){
        fun readAllCsvData(fileName: String) : List<Array<String>>{
            return try {
                FileReader("$filePath/$fileName").use { fr ->
                    CSVReader(fr).use {
                        it.readAll()
                    }
                }
            } catch (e : IOException){
                if (BuildConfig.DEBUG){
                    e.printStackTrace()
                }
                listOf()
            }
        }
    }

}