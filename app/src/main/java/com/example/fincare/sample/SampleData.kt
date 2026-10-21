package com.example.fincare.sample

import com.example.fincare.domain.Accommodation
import com.example.fincare.domain.Contact

val sampleDataAccommodation = Accommodation(
    total = 1840.0,
    rating = 4.93,
    imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1200&q=80",
    location = "Copacabana, Rio de Janeiro",
    host = "Ana",
    dates = "3 – 8 nov"
)

val sampleDataAccommodations: List<Accommodation> = listOf(
    Accommodation(
        total = 1840.0,
        rating = 4.93,
        imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1200&q=80",
        location = "Copacabana, Rio de Janeiro",
        host = "Ana",
        dates = "3 – 8 nov"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.81,
        imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1200&q=80",
        location = "Pinheiros, São Paulo",
        host = "Lucas",
        dates = "12 – 15 nov"
    ),
    Accommodation(
        total = 9260.0,
        rating = 4.97,
        imageUrl = "https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=1200&q=80",
        location = "Jurerê, Florianópolis",
        host = "Marina",
        dates = "20 – 27 dez"
    ),
    Accommodation(
        total = 729.0,
        rating = 4.74,
        imageUrl = "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&w=1200&q=80",
        location = "Savassi, Belo Horizonte",
        host = "Pedro",
        dates = "1 – 4 dez"
    ),
    Accommodation(
        total = 3160.0,
        rating = 4.99,
        imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1200&q=80",
        location = "Trancoso, Bahia",
        host = "Julia",
        dates = "8 – 15 jan"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.88,
        imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=1200&q=80",
        location = "Boa Viagem, Recife",
        host = "Rafael",
        dates = "18 – 22 nov"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.69,
        imageUrl = "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=1200&q=80",
        location = "Centro, Curitiba",
        host = "Camila",
        dates = "5 – 7 dez"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.95,
        imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80",
        location = "Praia do Forte, Mata de São João",
        host = "Bruno",
        dates = "26 dez – 2 jan"
    )
)

val sampleDataContact: Contact = Contact(
    name = "José",
    number = "(82) 99340-9784"
)

val sampleDataContacts: List<Contact> = listOf(
    Contact(
        name = "José",
        number = "(82) 99340-9784"
    ),
    Contact(
        name = "João",
        number = "(82) 99340-9783"
    ),
    Contact(
        name = "Pedro",
        number = "(82) 99340-9781"
    ),
    Contact(
        name = "Mariana",
        number = "(82) 99340-9785"
    ),
    Contact(
        name = "Aline",
        number = "(82) 99340-9787"
    ),
    Contact(
        name = "Lucas",
        number = "(82) 99340-9788"
    ),
)