package com.proyecto.proyectointegradomra.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BottomNavigationBar
import com.proyecto.proyectointegradomra.ui.common.Logo
import com.proyecto.proyectointegradomra.ui.common.StandardButton

@Composable
fun HomeView(
    dataRepository: DataRepository,
    navTo: NavHostController
) {
    val miUsuario by dataRepository.usuario.observeAsState()
    Scaffold(bottomBar = { BottomNavigationBar(navController = navTo) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            StandardButton(
                text = "",
                icon = Icons.Filled.Add,
                onClick = {})
        }
    }
}

@Preview
@Composable
fun HomeViewPreview() {
    var n by remember { mutableIntStateOf ( 0 ) }
    Scaffold(bottomBar = { BottomNavigationBar(navController = rememberNavController()) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.align(Alignment.TopCenter)) {
                Logo()
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(n) { item -> ClickableElevatedCardSample(item) }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(256.dp, 0.dp, 0.dp, 0.dp)
            ) {
                StandardButton(
                    text = "",
                    icon = Icons.Filled.Add,
                    onClick = { n++ }
                )
            }
        }
    }
}

@Composable
fun ClickableElevatedCardSample(int: Int) {
    ElevatedCard(
        onClick = { /* Do something */ },
        modifier = Modifier
            .size(width = 300.dp, height = 200.dp)
            .padding(16.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(text = "Hola $int", modifier = Modifier.align(Alignment.Center))
        }
    }
}