# LSM-based-Key-Value-Store
implementaion of SSTable from 3 part of book "Designing Data-Intensive Applications"

### ✅ Функциональные требования
Поддержка put, get, delete (TOMBSTONE).
Поиск самой свежей версии ключа.
Компакшен объединяет SSTable и удаляет старые данные.

### ✅ Нефункциональные требования
WAL для защиты данных от сбоев.
Использование fsync() для надёжной записи.
Разреженные индексы и Bloom-фильтры для ускоренного поиска.
Компакшен уменьшает размер SSTable и удаляет старые версии.
