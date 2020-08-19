package game.chessstrategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Random;

import game.Board;
import game.ChessStrategy;
import game.Game;
import game.Piece;
import game.Player;
import game.commands.Move;

public class RandomMove extends ChessStrategy 
{
	public RandomMove() 
	{
		
	}

	@Override
	public Move getPrefferedMove(Player player) 
	{
		Game game = player.getGame();
		
		Board board = game.getBoard();
		
		List<Move> allMoves = new ArrayList<Move>();
		
		Set<Piece> pieces = board.getPiecesFromPlayer(player);
		
		Iterator<Piece> itr = pieces.iterator();
		
		while(itr.hasNext())
		{
			Piece piece = itr.next();
			
			Set<Move> moves = piece.getAvailableMoves();
			
			Iterator<Move> movesItr = moves.iterator();
			
			while(movesItr.hasNext())
			{
				Move move = movesItr.next();
				
				allMoves.add(move);
			}
		}
		
		if(allMoves.isEmpty())
			return null;
		
		Random random = new Random();
		
		int index = random.nextInt(allMoves.size());
		
		return allMoves.get(index);
	}
	
	

}
