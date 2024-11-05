package com.example.damr_taller.FirebaseDatabase


import com.google.firebase.firestore.FirebaseFirestore

class FirebaseNameRepository {
    private val db = FirebaseFirestore.getInstance()
    private val namesCollection = db.collection("names")

    fun addName(name: Name, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        namesCollection.add(name)
            .addOnSuccessListener { documentReference ->
                name.id = documentReference.id
                onSuccess()
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun getNames(onSuccess: (List<Name>) -> Unit, onFailure: (Exception) -> Unit) {
        namesCollection.get()
            .addOnSuccessListener { result ->
                val names = result.toObjects(Name::class.java)
                onSuccess(names)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteAllNames(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        namesCollection.get()
            .addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    namesCollection.document(document.id).delete()
                }
                onSuccess()
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun nameExits(name: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        namesCollection.whereEqualTo("name", name).get()
            .addOnSuccessListener { result ->
                onSuccess(!result.isEmpty)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}