package com.example.common.RestApi.db;

import com.example.common.RestApi.db.entity.Quake;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface QuakeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Quake quake);

    @Query("DELETE FROM Quakes")
    void deleteAll();

    @Query("Select * from Quakes Order by timestamp DESC")
    LiveData<List<Quake>> getQuakes();
}
