package com.inspi.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.inspi.app.data.local.entities.FriendEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FriendDao_Impl implements FriendDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FriendEntity> __insertionAdapterOfFriendEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByCode;

  public FriendDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFriendEntity = new EntityInsertionAdapter<FriendEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `friends` (`code`,`username`,`hobby`,`weeklyXp`,`currentStreak`,`addedAt`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FriendEntity entity) {
        statement.bindString(1, entity.getCode());
        statement.bindString(2, entity.getUsername());
        statement.bindString(3, entity.getHobby());
        statement.bindLong(4, entity.getWeeklyXp());
        statement.bindLong(5, entity.getCurrentStreak());
        statement.bindLong(6, entity.getAddedAt());
      }
    };
    this.__preparedStmtOfDeleteByCode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM friends WHERE code = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FriendEntity friend, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFriendEntity.insert(friend);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByCode(final String code, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByCode.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, code);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByCode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FriendEntity>> observeAll() {
    final String _sql = "SELECT * FROM friends ORDER BY weeklyXp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"friends"}, new Callable<List<FriendEntity>>() {
      @Override
      @NonNull
      public List<FriendEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfHobby = CursorUtil.getColumnIndexOrThrow(_cursor, "hobby");
          final int _cursorIndexOfWeeklyXp = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyXp");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<FriendEntity> _result = new ArrayList<FriendEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FriendEntity _item;
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpHobby;
            _tmpHobby = _cursor.getString(_cursorIndexOfHobby);
            final int _tmpWeeklyXp;
            _tmpWeeklyXp = _cursor.getInt(_cursorIndexOfWeeklyXp);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new FriendEntity(_tmpCode,_tmpUsername,_tmpHobby,_tmpWeeklyXp,_tmpCurrentStreak,_tmpAddedAt);
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
  public Object findByCode(final String code,
      final Continuation<? super FriendEntity> $completion) {
    final String _sql = "SELECT * FROM friends WHERE code = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, code);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FriendEntity>() {
      @Override
      @Nullable
      public FriendEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfHobby = CursorUtil.getColumnIndexOrThrow(_cursor, "hobby");
          final int _cursorIndexOfWeeklyXp = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyXp");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final FriendEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpHobby;
            _tmpHobby = _cursor.getString(_cursorIndexOfHobby);
            final int _tmpWeeklyXp;
            _tmpWeeklyXp = _cursor.getInt(_cursorIndexOfWeeklyXp);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _result = new FriendEntity(_tmpCode,_tmpUsername,_tmpHobby,_tmpWeeklyXp,_tmpCurrentStreak,_tmpAddedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
