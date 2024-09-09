package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    var scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Text(text = "Tela Principal")
        var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
        var nome by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        var idParaRemover by remember { mutableStateOf("") }
        var idParaBuscar by remember { mutableStateOf("") }
        var usuarioBuscado by remember { mutableStateOf<Usuario?>(null) }



        LaunchedEffect(Unit) {
            scope.launch {
                Log.d("principal", "aqui")
                usuarios = getUsuarios()
            }
        }

        // Formulário para inserir um novo usuário
        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        TextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") })
        Button(onClick = {
            scope.launch {
                // Cria o novo usuário sem o campo 'id'
                val novoUsuario = Usuario()  // Usa o construtor vazio
                novoUsuario.nome = nome
                novoUsuario.senha = senha

                RetrofitClient.usuarioService.inserirUsuario(novoUsuario)
                usuarios = getUsuarios() // Atualiza a lista após inserção
            }
        }) {
            Text("Inserir Usuário")
        }



        // Campo para remover um usuário por ID
        TextField(value = idParaRemover, onValueChange = { idParaRemover = it }, label = { Text("ID para Remover") })
        Button(onClick = {
            scope.launch {
                try {
                    RetrofitClient.usuarioService.removerUsuario(idParaRemover)  // Trate o ID como String
                    usuarios = getUsuarios() // Atualiza a lista após remoção
                } catch (e: Exception) {
                    Log.e("TelaPrincipal", "Erro ao remover usuário: ${e.message}")
                }
            }
        }) {
            Text("Remover Usuário")
        }
        TextField(value = idParaBuscar, onValueChange = { idParaBuscar = it }, label = { Text("ID para Buscar") })
        Button(onClick = {
            scope.launch {
                try {
                    // Chama o serviço de busca por ID
                    usuarioBuscado = RetrofitClient.usuarioService.buscarPorId(idParaBuscar)

                    // Verifica se o usuário foi encontrado
                    if (usuarioBuscado != null) {
                        Log.d("BuscaUsuario", "Usuário encontrado: ${usuarioBuscado?.nome}")
                    } else {
                        Log.d("BuscaUsuario", "Usuário não encontrado")
                    }
                } catch (e: Exception) {
                    Log.e("TelaPrincipal", "Erro ao buscar usuário: ${e.message}")
                }
            }
        }) {
            Text("Buscar Usuário")
        }

// Exibe o resultado da busca
        usuarioBuscado?.let {
            Column {
                Text(text = "Nome: ${it.nome}")
                Text(text = "Senha: ${it.senha}")
                Text(text = "ID: ${it.id}")
            }
        }



        Button(onClick = { onLogoffClick() }) {
            Text("Sair")
        }

        // Carrega sob demanda à medida que o usuário rola na tela
        LazyColumn {
            items(usuarios) { usuario ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nome: ${usuario.nome}")
                        Text(text = "Senha: ${usuario.senha}")
                        Text(text = "ID: ${usuario.id}")
                    }
                }
            }
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}
