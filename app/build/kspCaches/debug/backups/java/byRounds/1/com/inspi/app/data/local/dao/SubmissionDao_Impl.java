package com.inspi.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.inspi.app.data.local.entities.SubmissionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubmissionDao_Impl implements SubmissionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubmissionEntity> __insertionAdapterOfSubmissionEntity;

  public SubmissionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubmissionEntity = new EntityInsertionAdapter<SubmissionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `submissions` (`id`,`imagePath`,`thumbnailPath`,`taskTitle`,`createdAt`,`hobbyType`,`xpEarned`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubmissionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getImagePath());
        statement.bindString(3, entity.getThumbnailPath());
        statement.bindString(4, entity.getTaskTitle());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindString(6, entity.getHobbyType());
        statement.bindLong(7, entity.getXpEarned());
      }
    };
  }

  @Override
  public Object insert(final SubmissionEntity submission,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSubmissionEntity.insertAndReturnId(submission);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubmissionEntity>> observeAll() {
    final String _sql = "SELECT * FROM submissions ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"submissions"}, new Callable<List<SubmissionEntity>>() {
      @Override
      @NonNull
      public List<SubmissionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final List<SubmissionEntity> _result = new ArrayList<SubmissionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubmissionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _item = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SubmissionEntity>> observeByHobby(final String hobby) {
    final String _sql = "SELECT * FROM submissions WHERE hobbyType = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, hobby);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"submissions"}, new Callable<List<SubmissionEntity>>() {
      @Override
      @NonNull
      public List<SubmissionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final List<SubmissionEntity> _result = new ArrayList<SubmissionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubmissionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _item = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id, final Continuation<? super SubmissionEntity> $completion) {
    final String _sql = "SELECT * FROM submissions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubmissionEntity>() {
      @Override
      @Nullable
      public SubmissionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final SubmissionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _result = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM submissions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomPastSubmission(final Continuation<? super SubmissionEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM submissions\n"
            + "        WHERE id != (SELECT id FROM submissions ORDER BY createdAt DESC LIMIT 1)\n"
            + "        ORDER BY RANDOM() LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubmissionEntity>() {
      @Override
      @Nullable
      public SubmissionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final SubmissionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _result = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLatest(final Continuation<? super SubmissionEntity> $completion) {
    final String _sql = "SELECT * FROM submissions ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubmissionEntity>() {
      @Override
      @Nullable
      public SubmissionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final SubmissionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _result = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRetakeCandidate(final long cutoffMillis, final String hobby,
      final Continuation<? super SubmissionEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM submissions\n"
            + "        WHERE createdAt < ? AND hobbyType = ?\n"
            + "        ORDER BY RANDOM() LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cutoffMillis);
    _argIndex = 2;
    _statement.bindString(_argIndex, hobby);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubmissionEntity>() {
      @Override
      @Nullable
      public SubmissionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final SubmissionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _result = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSubmissionsSince(final long weekStartMillis,
      final Continuation<? super List<SubmissionEntity>> $completion) {
    final String _sql = "SELECT * FROM submissions WHERE createdAt >= ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekStartMillis);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SubmissionEntity>>() {
      @Override
      @NonNull
      public List<SubmissionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfTaskTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "taskTitle");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHobbyType = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbyType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final List<SubmissionEntity> _result = new ArrayList<SubmissionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubmissionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final String _tmpThumbnailPath;
            _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            final String _tmpTaskTitle;
            _tmpTaskTitle = _cursor.getString(_cursorIndexOfTaskTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpHobbyType;
            _tmpHobbyType = _cursor.getString(_cursorIndexOfHobbyType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            _item = new SubmissionEntity(_tmpId,_tmpImagePath,_tmpThumbnailPath,_tmpTaskTitle,_tmpCreatedAt,_tmpHobbyType,_tmpXpEarned);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
